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

    //what about pages of events? there are more pages,and would explain why you dont count to 10 sometimes on vincents page

    private static final String BASE_URL = "https://api.github.com/users/";
//    private static final String TOKEN = "?&per_page=99";

    public static String eventsAsHTML(String user) throws IOException, ParseException {
        List<JSONObject> response = getEvents(user);
        StringBuilder sb = new StringBuilder();
        sb.append("<div>");

        int pushCount = 1;
        String pushType = "PushEvent";

        for (int i = 0; i < response.size(); i++) {
            JSONObject event = response.get(i);

          if (pushType.equalsIgnoreCase(event.getString("type")) && pushCount <11){
                pushCount++;
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

                //changes
                //changes
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
        String pageArg = "&page=";
        int pageNum = 1;
        boolean JSONArrayEmpty = false;
        List<JSONObject> eventList = new ArrayList<JSONObject>();



        while (JSONArrayEmpty == false) {
            String url = BASE_URL + user + "/events?" + pageArg + pageNum; //need to add token here
            System.out.println(url);
            JSONObject json = Util.queryAPI(new URL(url));
            System.out.println(json);
            JSONArray events = json.getJSONArray("root");

            if (events.length() == 0) {
                JSONArrayEmpty = true;
            } else {
                for (int i = 0; i < events.length() && i < 10; i++) {
                    eventList.add(events.getJSONObject(i));
                }
                pageNum++;
            }
        }
        return eventList;

    }
}