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
public class Old {
	@JsonProperty("id")
	private String id;
	@JsonProperty("name")
	private String name;
	// closed = true - archived, false - not archived
	@JsonProperty("closed")
	private boolean closed;
}
