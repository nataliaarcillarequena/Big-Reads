package io.javabrains.bigreads.searchBook;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchBook {
	
	private String key, title;
	private List<String> author_name;
	private String coverId;
	private String cover_i;
	private int first_publish_year;
	 

}
