package listeners.servlets;

//10.9
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import listeners.trello.json.ActionData;
import listeners.trello.json.Model;
import listeners.trello.json.RequestBody;
import listeners.trello.json.Webhook;
import listeners.trello.json.Wrapper;

/**
 * Servlet implementation class TrelloServlet NOTE: if pref and labelName in the
 * json are not in use, delete them.
 */
@WebServlet("/Trello")
public class TrelloServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// Check out if its fine that this is hardcoded
	private final String API_KEY = "c81b2108691c2859d7cd9174e925fbe5";

	private MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
	private RestTemplate restTemplate = new RestTemplate();
	private HttpEntity<String> request;
	private ResponseEntity<Webhook> result;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TrelloServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/*
	 * 
	 * @see javax.servlet.http.HttpServlet#doHead(javax.servlet.http.
	 * HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 * 
	 * The Trello webhooks must confirm the callBackUrl, doing so by using HEAD
	 * request, if result is 200 than the webhook will be created
	 */
	protected void doHead(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		response.setStatus(HttpServletResponse.SC_OK);
		out.println("ok");
		out.close();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			handleRequest(request, response);
		} catch (InterruptedException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void handleRequest(HttpServletRequest request, HttpServletResponse response)
			throws IOException, InterruptedException, ParseException {
		PrintWriter out = response.getWriter();
		JSONObject responseJson = new JSONObject();
		// Get the request body - the body should contain JSON with all the
		// details about the even that has occurred
		String requestBody = IOUtils.toString(request.getInputStream());
		responseJson = buildMessage(requestBody);
		sendMessage(responseJson);
		out.print(responseJson);
		out.close();

	}

	/*
	 * Building the message for the user according to the template adding the
	 * message to the response json and return it to handleRequest.
	 */
	private JSONObject buildMessage(String requestBody) {
		JSONObject responseJson = new JSONObject();
		Wrapper webhookRequest = new Gson().fromJson(requestBody, Wrapper.class);
		boolean closed = false; // archived or not
		String listId = "", listName = "", cardName = "", comment = "";
		String boardId = webhookRequest.getModel().getId();
		String url = webhookRequest.getModel().getUrl();
		String type = webhookRequest.getAction().getType();
		String dateString = webhookRequest.getAction().getDate();
		// Fixing the date format - removing T and Z so I will be able to format
		// them for the user
		dateString = StringUtils.replace(StringUtils.replace(dateString, "T", " "), "Z", " ");
		Date date = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
		try {
			date = df.parse(dateString);
		} catch (Exception e) {

		}

		ActionData data = webhookRequest.getAction().getData();
		if (data.getCard() != null) {
			cardName = data.getCard().getName();
			comment = data.getText();
			// If the old field exists meanning the item was moved\restored from
			// archive
			if (data.getOld() != null) {
				closed = data.getCard().isClosed();
			}

		}
		if (data.getList() != null) {
			listId = "list:" + data.getList().getId();
			listName = data.getList().getName();
			// If the old field exists meanning the item was moved\restored from
			// archive
			if (data.getOld() != null) {
				closed = data.getList().isClosed();
			}
		}
		/*
		 * We do not mind if something has been deleted, only if something was
		 * created or updated\changed.
		 */
		StringBuilder builder = new StringBuilder();
		switch (type) {
		case "createCard":
			builder.append("Card \"" + cardName + "\" was Created \n ");

			break;
		case "updateCard":
			builder.append("Card \"" + cardName + "\" was Updated \n ");
			break;
		case "commentCard":
			builder.append("New comment for \"" + listName + "\" \n ");
			builder.append("Card \"" + cardName + "\" \n ");
			builder.append(date + " \n ");
			builder.append("By: " + webhookRequest.getAction().getMemberCreator().getFullName() + " \n ");
			builder.append(comment + " \n ");

			break;
		case "createList":
			builder.append("List " + listName + " was created \n ");
			builder.append(date + " \n ");
			break;
		case "updateList":
			// List can be archived or renamed

			if (closed) {
				builder.append("List " + listName + " was archived \n");
			} else {
				builder.append("List " + data.getOld().getName() + " was renamed \n ");
				builder.append("New name: " + listName);
			}

			builder.append(date + " \n ");
			break;
		case "updateComment":
			builder.append("Comment in " + listName + "was edited \n ");
			builder.append("Edited text: " + data.getAction().getText() + " \n ");
			break;

		}
		builder.append("URL: " + url);
		// setting the response json
		responseJson.put("objectId", boardId);
		responseJson.append("messages", new JSONObject().put("itemId", listId).append("details", builder.toString()));

		return responseJson;
	}

	private void sendMessage(JSONObject messageBody) {
		String url = " https://stagingsite.availo.me:435/chat.svc/TRELLO/chat/messages";
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		HttpEntity<String> request = new HttpEntity<String>(messageBody.toString(), header);
		HttpEntity<String> result = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

	}
}
