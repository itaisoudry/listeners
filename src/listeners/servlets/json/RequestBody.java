package listeners.servlets.json;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestBody {
	private String description;
	private String idModel;
	private String callBackUrl;
	private String userToken;
	
}
