package listeners.trello.json;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data

public class Model {
	@JsonProperty("id")
	private String id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("idOrganization")
	private String idOrganization;
	@JsonProperty("url")
	private String url;
	@JsonProperty("shortUrl")
	private String shortUrl;
	@JsonProperty("prefs")
	private Prefs prefs;
	@JsonProperty("labelName")
	private LabelNames labelName;
}
