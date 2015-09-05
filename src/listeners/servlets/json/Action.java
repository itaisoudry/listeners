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
public class Action {
	@JsonProperty("id")
	private String id;
	@JsonProperty("idMemberCreator")
	private String idMemberCreator;
	@JsonProperty("type")
	private String type; // (createCard,udpateCard,deleteCard,createList,updateList
	@JsonProperty("date")
	private String date; // (format yyyy-mm-ddThh:mm:ss.sssZ)
	@JsonProperty("data")
	private Data data;
	@JsonProperty("board")
	private Board board;
	@JsonProperty("list")
	private List list;
	@JsonProperty("card")
	private Card card;
	@JsonProperty("memberCreator")
	private MemberCreator memberCreator;

}
