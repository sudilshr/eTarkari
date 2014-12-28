package com.etarkari.maps;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class GoogleMapper {
	   @SerializedName("debug_info")
	   private List<String> debug_info;

	   @SerializedName("html_attributions")
	   private List<String> html_attributions;

	   @SerializedName("next_page_token")
	   private String next_page_token;

	   @SerializedName("results")
	   private List<Results> results;

	   public List<String> getDebugInfo() {
	      return debug_info;
	   }

	   public List<String> getHtmlAttributions() {
	      return html_attributions;
	   }

	   public String getNextPageToken() {
	      return next_page_token;
	   }

	   public List<Results> getResults() {
	      return results;
	   }

	   public String getStatus() {
	      return status;
	   }

	   @SerializedName("status")
	   private String status;
	}