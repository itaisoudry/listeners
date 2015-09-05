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
public class LabelNames {
	@JsonProperty("green")
	private String green;
	
	@JsonProperty("yellow")
	private String yellow;

	@JsonProperty("orange")
	private String orange;

	@JsonProperty("red")
	private String red;

	@JsonProperty("purple")
	private String purple;

	@JsonProperty("blue")
	private String blue;

	@JsonProperty("sky")
	private String sky;

	@JsonProperty("lime")
	private String lime;

	@JsonProperty("pink")
	private String pink;

	@JsonProperty("black")
	private String black;

}
