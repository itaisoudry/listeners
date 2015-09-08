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
public class ActionData {
	@JsonProperty("board")
	private Board board;
	@JsonProperty("list")
	private List list;
	@JsonProperty("card")
	private Card card;
	@JsonProperty("oldS")
	private Old old;
	@JsonProperty("text")
	private String text;
	// for updateComment
	@JsonProperty("action")
	private Action action;
}
