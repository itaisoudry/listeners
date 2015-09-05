package listeners.servlets.json;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberCreator {
	private String id;
	private String fullName;
	private String username;
}
