package ecs189.querying.github;

import ecs189.querying.Util;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Vincent on 10/1/2017.
 */
public class GithubQuerier {

    private static final String BASE_URL = "https://api.github.com/users/";
    private static final String TOKEN = "#access_token=cbe55b6bd013e096925e134e0f70ede3803ac8c0";

    public static String eventsAsHTML(String user) throws IOException, ParseException {
        List<JSONObject> response = getEvents(user);
        StringBuilder sb = new StringBuilder();
        sb.append("<div>");

        String pushType = "PushEvent";

        for (int i = 0; i < response.size(); i++) {
            JSONObject event = response.get(i);

          if (pushType.equalsIgnoreCase(event.getString("type"))){
                // Get event type
                String type = event.getString("type");

                // Get created_at date, and format it in a more pleasant style
                String creationDate = event.getString("created_at");
                SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
                SimpleDateFormat outFormat = new SimpleDateFormat("dd MMM, yyyy");
                Date date = inFormat.parse(creationDate);
                String formatted = outFormat.format(date);

                // Add type of event as header
                sb.append("<h3 class=\"type\">");
                sb.append(type);
                sb.append("</h3>");

                // Add formatted date
                sb.append(" on ");
                sb.append(formatted);
                sb.append("<br />");


                // Add collapsible JSON textbox (don't worry about this for the homework; it's just a nice CSS thing I like)
                sb.append("<a data-toggle=\"collapse\" href=\"#event-" + i + "\">JSON</a>");
                sb.append("<div id=event-" + i + " class=\"collapse\" style=\"height: auto;\"> <pre>");
                sb.append(event.toString());
                sb.append("</pre> </div>");

                //a vertically formatted list of all its commits with their SHA ( abbrev to first 8 characters)
                // and their commit message
                JSONObject payload = event.getJSONObject("payload");
                if(payload.has("commits")){

                    JSONArray commitArray;
                    commitArray = payload.getJSONArray("commits");

                    sb.append("<p>Commit and \"Message\" </p> ");

                    for (int j = 0; j < commitArray.length(); j++) {


                        JSONObject jsonobject = commitArray.getJSONObject(j);
                        String sha = jsonobject.getString("sha");
                        String message = jsonobject.getString("message");
                        int counter = j+1;

                        sb.append("<p>" + counter +") ");
                        sb.append(sha.substring(0,8));
                        sb.append(" \"" + message +"\"");
                        sb.append("</p>");
                    }
                }



          }


        }
        sb.append("</div>");
        return sb.toString();
    }

    private static List<JSONObject> getEvents(String user) throws IOException {
        List<JSONObject> eventList = new ArrayList<JSONObject>();
        String url = BASE_URL + user + "/events" + TOKEN; //need to add token here
        System.out.println(url);
        JSONObject json = Util.queryAPI(new URL(url));
        System.out.println(json);
        JSONArray events = json.getJSONArray("root");
        for (int i = 0; i < events.length() && i < 10; i++) {
            eventList.add(events.getJSONObject(i));
        }
        return eventList;
    }
}