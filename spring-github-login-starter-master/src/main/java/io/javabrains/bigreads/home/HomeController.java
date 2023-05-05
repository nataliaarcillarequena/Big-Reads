package io.javabrains.bigreads.home;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import io.javabrains.bigreads.user.BookByUserRepository;
import io.javabrains.bigreads.user.BooksByUser;

@Controller
public class HomeController {

	@Autowired
	BookByUserRepository booksByUserRepo;
	
	
	@RequestMapping("/home")
	public ModelAndView homePage(@AuthenticationPrincipal OAuth2User principal) {
		
		ModelAndView modelView = new ModelAndView();
		
		//if user logs insuccessfully then go to home page, otherwise stay on index page
		if(principal != null && principal.getAttribute("login")!=null) {
			modelView.setViewName("home");
			
			String userId = principal.getAttribute("login");
			//pageable instance we need to create is a cassandra page instance- start from page 0 and give the 
			//first 20 records 
			Slice<BooksByUser> booksSlice = booksByUserRepo.findAllById(userId, CassandraPageRequest.of(0, 20));
			//get content out of slice- gives list of BooksByUser obj
			List<BooksByUser> booksByUser = booksSlice.getContent();
			modelView.addObject("booksByUser", booksByUser);
			modelView.addObject("loginId", principal.getAttribute("login"));
			
			
		}else {
			modelView.setViewName("index");
		}
		
		return modelView;
	}
	
//	@RequestMapping("/logout")
//	public ModelAndView logout() {
//		return new ModelAndView("index");
//	}
	
	
}
