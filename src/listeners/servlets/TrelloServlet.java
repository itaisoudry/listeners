package listeners.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;

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

import listeners.servlets.json.RequestBody;
import listeners.servlets.json.Webhook;
import listeners.servlets.json.Wrapper;

/**
 * Servlet implementation class TrelloServlet
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

	private String addWebhookUrl = "https://trello.com/1/tokens/[USER_TOKEN]/webhooks/?key=[APPLICATION_KEY]";
	private String delWebhookUrl = "https://trello.com/1/webhooks/[WEBHOOK_ID]?key=[APPLICATION_KEY]&token=[USER_TOKEN]";
	private String command;

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

		breakDownUri(request.getPathInfo().toString());

		switch (command) {
		// add webhook command
		case "add":
			out.println(addAccount(requestBody));

			break;
		}

		out.close();
	}

	/*
	 * Creating a new webhook for a model in trello userToken - used for
	 * registering the webhook in trello - unique for each user description -
	 * webhook description callBackUrl - should be always nir's address idModel
	 * - the id of the model that we want to connect the webhook with
	 */
	private JSONObject addAccount(String body) {
		JSONObject requestBody = new JSONObject();
		JSONObject responseJson = new JSONObject();
		header.add("Content-Type", "application/json");

		RequestBody myResult = new Gson().fromJson(body, RequestBody.class);
		String url = StringUtils.replace(StringUtils.replace(addWebhookUrl, "[USER_TOKEN]", myResult.getUserToken()),
				"[APPLICATION_KEY]", API_KEY);

		// set body json
		requestBody.put("description", myResult.getDescription());
		requestBody.put("callbackURL", myResult.getCallBackUrl());
		requestBody.put("idModel", myResult.getIdModel());
		// execute request
		request = new HttpEntity<String>(requestBody.toString(), header);
		result = restTemplate.exchange(url, HttpMethod.POST, request, Webhook.class);

		// set json to return
		responseJson.put("id", result.getBody().getId());
		responseJson.put("description", result.getBody().getDescription());
		responseJson.put("idModel", result.getBody().getIdModel());
		responseJson.put("callbackURL", result.getBody().getCallbackURL());
		responseJson.put("active", result.getBody().isActive());

		return responseJson;

	}

	/*
	 * Deleting an existing webhook in trello
	 */
	private void removeAccount(String userToken, String webhookId) {

	}

	private void breakDownUri(String uri) {
		try {
			command = StringUtils.substringBeforeLast(uri, "/");
		} catch (Exception e) {
			command = null;
		}
	}

}
