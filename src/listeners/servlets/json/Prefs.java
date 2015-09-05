package listeners.servlets.json;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Prefs {
	@JsonProperty("permissionLevel")
	private String permissionLevel;
	@JsonProperty("comments")
	private String comments;

}
