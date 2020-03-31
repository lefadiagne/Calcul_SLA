package com.lbp.sla.niveau.de.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

@SpringBootApplication
public class NiveauDeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NiveauDeServiceApplication.class, args);

	}
	@Bean
	public RestTemplate getRestTemplate(){
		return new RestTemplate();
	}



}

//  int compteur = 0;
//        Calendar calendar1 = new GregorianCalendar();
//        calendar1.set(Calendar.YEAR, 2009);
//        calendar1.set(Calendar.MONTH, 5);
//        calendar1.set(Calendar.DAY_OF_MONTH, 15);
//        Date date1 = calendar1.getTime();
//
//        //  2006-08-15
//        Calendar calendar2 = new GregorianCalendar();
//        calendar2.set(Calendar.YEAR, 2009);
//        calendar2.set(Calendar.MONTH, 5);
//        calendar2.set(Calendar.DAY_OF_MONTH, 28);
//        Date date2 = calendar2.getTime();
//
//        // Différence
//        long diff = Math.abs(date2.getTime() - date1.getTime());
//        long numberOfDay = (long)diff/86400000;
//        System.out.println("Le nombre de jour est : " + numberOfDay);
//
//         for(int i=0;i<=numberOfDay;i++){
//             if(calendar1.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && calendar1.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY)
//                compteur++;
//             calendar1.add(Calendar.DAY_OF_MONTH, 1);
//         }
//        System.out.println("Le nombre de jour ouvrés est : " + compteur);