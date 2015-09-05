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
public class Webhook {
	@JsonProperty("id")
	private String id;
	@JsonProperty("description")
	private String description;
	@JsonProperty("idModel")
	private String idModel;
	@JsonProperty("callbackURL")
	private String callbackURL;
	@JsonProperty("active")
	private boolean active;
}
