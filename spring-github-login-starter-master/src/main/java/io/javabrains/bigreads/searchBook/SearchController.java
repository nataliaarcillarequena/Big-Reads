package io.javabrains.bigreads.searchBook;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;

import reactor.core.publisher.Mono;

@Controller
public class SearchController {

	private final WebClient webClient;
	
	//https://openlibrary.org/search.json?q=the+lord+of+the+rings - the pattern we need to search a book using open library API 
	
	//so we just build the web client once, then keep it in search controller to that everytime someone makes a search
	//request, web client builder is used- dont have to re-build
	public SearchController(WebClient.Builder webClientBuilder) {
		//the webclient builder makes calls to the base url 
		//exchange stratergies extends the limit of the amount of buffer enables in the web client request and response 
		//exchange- bump up the upper limit- web client will have more memory so that it doesnt run out of space 
		this.webClient = webClientBuilder.exchangeStrategies(ExchangeStrategies.builder()
					.codecs(configurer -> configurer
							.defaultCodecs()
							.maxInMemorySize(16*1024*1024))
				.build()).baseUrl("https://openlibrary.org/search.json").build();
	}
	
	//after clicking on the search, this should map to the relevant book page; books/id (in the BookController)
	@RequestMapping("/search")
	public ModelAndView search(@RequestParam("searchInput")String search, @AuthenticationPrincipal OAuth2User principal) {
		
		ModelAndView modelAndView = new ModelAndView();
		
		if(principal != null && principal.getAttribute("login") != null) {
			modelAndView.addObject("loginId", principal.getAttribute("login"));
		}  //can now use this to add the log out button if logged in user
		
		
		
		//need to make a call to: https://openlibrary.org/search.json?q=the+lord+of+the+rings- to get the books we want 
		//web client is best way to make API calls - create web client in constructor to not have to do it every time we have a request
		
		//when someone makes search request- append it to the url (web client one) and sent it across 
		//this. = current object in method/constructor 
		//.get() starts the creation of the http request - get the request now make the pattern - so building a request for the already built
		//web client 
		Mono<SearchResult> obj = this.webClient.get().uri("?q={search}",search)
		//retrieve makes the call	
		//bodyToMono makes the obj a mono- mono is an object with a single result - with type string- but instead, can do a type 'SearchResult' which
		//has the fields we want from the open library json object, then we parse straight to that instead of parsing from a string- must easier -
		//the framework does the conversion for us 
		.retrieve().bodyToMono(SearchResult.class);
		
		//block() gets the string of the mono 
		SearchResult searchResult = obj.block();
		
		//there will be a data limit exception- so need to widen limit to get all the records from the open library API for the search- do it in the constructor
		
		//get the docs array from the search result- stream it and limit the output and put it back in a list
		List<SearchBook> books = searchResult.getDocs()
			.stream()
			.limit(10)
			//mapping the keys so that they are in the required format - !!!!!!!!!!! also do it for the covers
			.map(bookResult -> {
				bookResult.setKey(bookResult.getKey().replace("/works/", ""));
				return bookResult;
			})
			.collect(Collectors.toList());
		
		modelAndView.addObject("searchResult", books);
		modelAndView.setViewName("search");
		//message for number of books found from search
		if(books.size()==1) {
			modelAndView.addObject("ResultMessage", books.size()+" result found");
		}else {
			modelAndView.addObject("ResultMessage", books.size()+" results found");
		}
		
		
		return modelAndView;
	}
	
	
	
	
	
	
}
