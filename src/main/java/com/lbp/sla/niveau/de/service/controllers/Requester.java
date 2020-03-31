package com.lbp.sla.niveau.de.service.controllers;

import com.lbp.sla.niveau.de.service.model.JiraTicket;
import com.lbp.sla.niveau.de.service.model.jira;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.codec.StringDecoder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RestController
@RequestMapping(value="/jira/tickets")
public class Requester {

    @Autowired
    RestTemplate restTemplate;

    @Value("${liste}")
    private String[] laListe;

    @GetMapping("/")
    public String getTicketsList() {
        HttpHeaders headers = createHttpHeaders("A772702","Galsen@99");
        //headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>("parameters",headers);
        return restTemplate.exchange("https://jira.worldline.com/rest/api/2/issue/MTSRLPPROD-1208?expand=changelog", HttpMethod.GET, entity, String.class).getBody();

    }

    @GetMapping("/{nameProject}/{listTicket}")
    public jira getTicketsListByID(@PathVariable(name = "nameProject") String nameProject, @PathVariable(name = "listTicket") String listTicket)  throws JSONException {

        String []ticketList = listTicket.split("-");
        long gtiGlobalDays=0;
        long gtiGlobalMin =0, gtiGlobalHour=0;
        long gtrGlobalHour =0;
        long gtrGlobalDays=0, gtrGlobalMin=0;

        int g = 0;

        for (int i=0; i<ticketList.length; i++){
            HttpHeaders headers = createHttpHeaders("A772702","Galsen@99");
            //headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<String>("parameters",headers);
            JSONObject jsonObj = new JSONObject(restTemplate.exchange("https://jira.worldline.com/rest/api/2/issue/" +nameProject+"-"+ticketList[i]+"?expand=changelog", HttpMethod.GET, entity, String.class).getBody());
            JSONObject vCreated = jsonObj.getJSONObject("fields");
            String creation = vCreated.getString("created");
            //String resolution = vCreated.getString("resolutiondate");
            String taken = vCreated.getString("customfield_22140");

            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
             LocalDateTime dateCreated = LocalDateTime.parse(creation, format);
             LocalDateTime dateTaken = LocalDateTime.parse(taken, format );

            long transHour=0, transMin=0;
            long transDays=0, trans1=0;
            long gti, gtiDays, gtiHour, gtiMin;
            long transGlobDays, transGlobHour, transGlobMin;
            long trans=0;

            gti = ChronoUnit.MINUTES.between(dateCreated, dateTaken);
            gtiDays = jourouvrable(gti, dateCreated);
            gtiHour = heureOuvrable(gti, dateCreated);
            gtiMin = minuteOuvrable(gti, dateCreated);

            System.out.println("\n\r"+"gti du MTSRLPPROD-"+ticketList[i]+" est "+gtiDays+" jour(s) "+gtiHour+" heure(s) "+gtiMin+"minute(s)"+"\n\r");

            gtiGlobalDays =gtiGlobalDays + gtiDays;
            gtiGlobalHour = gtiGlobalHour + gtiHour;
            gtiGlobalMin = gtiGlobalMin + gtiMin;

            JSONArray  tabHis= jsonObj.getJSONObject("changelog").getJSONArray("histories");

            String fields = "status";

            List<String> list = new ArrayList<String>();
            int k=0;
            for (int j=0;j<tabHis.length();j++) {

                JSONObject obHis = tabHis.getJSONObject(j);
                JSONArray arrItems = obHis.getJSONArray("items");

                    for (int d=0; d<arrItems.length();d++) {
                        JSONObject obitems = arrItems.getJSONObject(d);

                        String field = obitems.getString("field");
                        if (field.equals(fields)) {
                            list.add(obHis.getString("created"));

                            k++;
                        }
                    }
            }

                    for (int b=0;b<list.size();b++){
                        if (b>0) {

                            if (b % 2 != 0) {
                                LocalDateTime dateTransition1 = LocalDateTime.parse(list.get(b-1), format);
                                LocalDateTime dateTransition2 = LocalDateTime.parse(list.get(b), format);
                                trans += ChronoUnit.MINUTES.between(dateTransition1, dateTransition2);
                                trans1 =trans;
                                transDays = jourouvrable(trans, dateTransition1);
                                transHour = heureOuvrable(trans, dateTransition1);
                                transMin = minuteOuvrable(trans, dateTransition1);
                            /*if (b % 2 == 0) {

                                LocalDateTime dateTransition3 = LocalDateTime.parse(list.get(b-1), format);
                                LocalDateTime dateTransition4 = LocalDateTime.parse(list.get(b), format);
                                neg += ChronoUnit.MINUTES.between(dateTransition3, dateTransition4);

                                //    negHour += ChronoUnit.HOURS.between(dateTransition1, dateTransition2);
                                negDays += jourouvrable(neg, dateTransition3);
                                negHour += heureOuvrable(neg, dateTransition3);
                                negMin += minuteOuvrable(neg, dateTransition3);

                            */
                            }

                        }

                }

            //transGlob = trans - neg;
            transGlobDays = transDays;  //- negDays;
            transGlobHour = transHour ; //- negHour;
            transGlobMin = transMin; // - negMin;
            jira j = new jira(gtiDays,gtiHour,gtiMin,transGlobDays,transGlobHour,transGlobMin);
            j.toString();
            System.out.println("gtr du MTSRLPPROD-"+ticketList[i]+" est "+transGlobDays+" jour(s) "+transGlobHour+" heure(s) "+transGlobMin+"minute(s)"+"\n\r");

            gtrGlobalDays = gtrGlobalDays + transGlobDays;
            gtrGlobalHour = gtrGlobalHour + transGlobHour;
            gtrGlobalMin = gtrGlobalMin + transGlobMin;


        }


        System.out.println("----------\n");
        System.out.println("GTI global = "+gtiGlobalDays+" jour(s) "+gtiGlobalHour+" heure(s) "+gtiGlobalMin+" minute(s)");
        System.out.println("GTR global : "+gtrGlobalDays+"jour(s) "+gtrGlobalHour+" heure(s) "+gtrGlobalMin+" minute(s)");

        return new jira(gtiGlobalDays,gtiGlobalHour,gtiGlobalMin,gtrGlobalDays,gtrGlobalHour,gtrGlobalMin);

    }

    @GetMapping("/{listTicket}")
    public JiraTicket getTicketsList( @PathVariable(name = "listTicket") String listTicket)  throws JSONException {




            HttpHeaders headers = createHttpHeaders("A772702","Galsen@99");
            //headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<String>("parameters",headers);
            JSONObject jsonObj = new JSONObject(restTemplate.exchange("https://jira.worldline.com/rest/api/2/issue/" +listTicket+"?expand=changelog", HttpMethod.GET, entity, String.class).getBody());
            JSONObject vCreated = jsonObj.getJSONObject("fields");
            String creation = vCreated.getString("created");
            //String resolution = vCreated.getString("resolutiondate");
            String taken = vCreated.getString("customfield_22140");

            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            LocalDateTime dateCreated = LocalDateTime.parse(creation, format);
            LocalDateTime dateTaken = LocalDateTime.parse(taken, format );

            long transHour=0, transMin=0;
            long transDays=0, trans1=0;
            long gti, gtiDays, gtiHour, gtiMin;
            long transGlobDays, transGlobHour, transGlobMin;
            long trans=0;

            gti = ChronoUnit.MINUTES.between(dateCreated, dateTaken);
            gtiDays = jourouvrable(gti, dateCreated);
            gtiHour = heureOuvrable(gti, dateCreated);
            gtiMin = minuteOuvrable(gti, dateCreated);

            System.out.println("\n\r"+"gti du MTSRLPPROD-"+listTicket+" est "+gtiDays+" jour(s) "+gtiHour+" heure(s) "+gtiMin+"minute(s)"+"\n\r");

            JSONArray  tabHis= jsonObj.getJSONObject("changelog").getJSONArray("histories");

            String fields = "status";

            List<String> list = new ArrayList<String>();
            int k=0;
            for (int j=0;j<tabHis.length();j++) {

                JSONObject obHis = tabHis.getJSONObject(j);
                JSONArray arrItems = obHis.getJSONArray("items");

                for (int d=0; d<arrItems.length();d++) {
                    JSONObject obitems = arrItems.getJSONObject(d);

                    String field = obitems.getString("field");
                    if (field.equals(fields)) {
                        list.add(obHis.getString("created"));

                        k++;
                    }
                }
            }

            for (int b=0;b<list.size();b++){
                if (b>0) {

                    if (b % 2 != 0) {
                        LocalDateTime dateTransition1 = LocalDateTime.parse(list.get(b-1), format);
                        LocalDateTime dateTransition2 = LocalDateTime.parse(list.get(b), format);
                        trans += ChronoUnit.MINUTES.between(dateTransition1, dateTransition2);
                        trans1 =trans;
                        transDays = jourouvrable(trans, dateTransition1);
                        transHour = heureOuvrable(trans, dateTransition1);
                        transMin = minuteOuvrable(trans, dateTransition1);
                    }

                }

            }



            System.out.println("gtr du MTSRLPPROD-"+listTicket+" est "+transDays+" jour(s) "+transHour+" heure(s) "+transMin+"minute(s)"+"\n\r");

        return new JiraTicket(listTicket,gtiDays,gtiHour,gtiMin,transDays,transHour,transMin);

    }



    private HttpHeaders createHttpHeaders(String user, String password)
    {
        String notEncoded = user + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(notEncoded.getBytes());
        HttpHeaders headers = new HttpHeaders();
        //headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
       // headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic " + encodedAuth);
        return headers;
    }

    private long jourouvrable(long nbrMin, LocalDateTime date1)
    {
        int compteur = 0;
        long r;
        Calendar calendar1= Calendar.getInstance();
        Date dateloc1 = toDate(date1);
        calendar1.setTime(dateloc1);

        long numberOfDay =nbrMin /1440;
        long numberOfMin = nbrMin;
        int year, month, day ;
        String dat;
        if (numberOfDay !=0) {

            for (int i = 0; i < numberOfDay; i++) {
                year = calendar1.get(Calendar.YEAR);
                month = calendar1.get(Calendar.MONTH);
                day = calendar1.get(Calendar.DATE);
                dat = day+"/"+month+"/"+year;
                for (String s : laListe) {
                    if (dat.equals(s))
                        numberOfMin = numberOfMin - 1740;
                }
                    if (calendar1.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY )
                        numberOfMin = numberOfMin - 1740;

                    else if (calendar1.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
                        numberOfMin = numberOfMin - 540;
                    else
                        numberOfMin = numberOfMin - 900;

                calendar1.add(Calendar.DAY_OF_MONTH, 1);

                }

        }

         r= numberOfMin %1440;

        return (numberOfMin - r)/1440;
    }
    private static Date toDate(LocalDateTime dateToConvert) {
        return Date.from( dateToConvert.atZone( ZoneId.systemDefault()).toInstant());
    }

    private long heureOuvrable(long nbrMin, LocalDateTime date1)
    {
        int compteur = 0;
        long r;
        Calendar calendar1= Calendar.getInstance();
        Date dateloc1 = toDate(date1);
        calendar1.setTime(dateloc1);
        long numberOfDay =nbrMin /1440;
        long numberOfMin = nbrMin;

        if (numberOfDay !=0) {
            for (int i = 0; i < numberOfDay; i++) {
                if (calendar1.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)//
                    numberOfMin = numberOfMin - 1740;
                else if (calendar1.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
                    numberOfMin = numberOfMin - 540;
                else
                    numberOfMin = numberOfMin - 900 ;

                calendar1.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        r= numberOfMin %1440;
        long numberOfDayFin = (numberOfMin - r)/1440;
        long number=r;
        r=number%60;
        //System.out.println("Le nombre d'heure ouvrés est : " + numberOfHour)
        return (number - r)/60;
    }
    private long minuteOuvrable(long nbrMin, LocalDateTime date1)
    {
        int compteur = 0;
        long r;
        Calendar calendar1= Calendar.getInstance();
        Date dateloc1 = toDate(date1);
        calendar1.setTime(dateloc1);
        long numberOfDay =nbrMin /1440;
        long numberOfMin = nbrMin;

        if (numberOfDay !=0) {
            for (int i = 0; i <= numberOfDay; i++) {
                if (calendar1.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)//
                    numberOfMin = numberOfMin - 1740;
                else if (calendar1.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
                    numberOfMin = numberOfMin - 540;
                else
                    numberOfMin = numberOfMin - 900 ;

                calendar1.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        r= numberOfMin %1440;
        //long numberOfDayFin = (numberOfMin - r)/1440;
        long number=r;
        //long numberOfHour = (number - r)/60;
        r=number%60;
        return r;
    }


}