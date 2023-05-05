package io.javabrains.bigreads.searchBook;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResult {

	//we need the docs array as this holds all the book information- and the information that
	//we need from that docs array if defined in the SearchBook class- as we dont want all the info
	//just the info that we are interested in
	private int numFound; //this is not in the docs array, just above it as a number
	private List<SearchBook> docs;
	
	
	
	
}
