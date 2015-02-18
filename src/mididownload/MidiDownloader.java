package mididownload;

import java.io.File;
import java.net.*;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import org.apache.commons.io.*;


public class MidiDownloader {

	public final static String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2227.1 Safari/537.36";
	//public final static String MIDI_DIRECTORY = "/Users/KarinBrotjefors/Desktop//MIDIdata/MIDIdata - free/"; public final static String URL = "http://midi-files-for-free.biz/";
	public final static String MIDI_DIRECTORY = "/Users/KarinBrotjefors/Desktop/MIDIdata/MIDIdata - mididb/"; public final static String URL = "http://mididb.com/pop/";


	public MidiDownloader() throws IOException{

		Elements links1 = getLinks(URL);
		if(checkUrl(URL)){
			for (Element link1 : links1) {
				String href1 = checkExtAndDownload(link1, 1);
				Elements links2 = getLinks(href1);
				if(checkUrl(href1)){
					for (Element link2: links2){
						String href2 = checkExtAndDownload(link2, 2);
						Elements links3 = getLinks(href2);
						if(checkUrl(href2)){
							for (Element link3 : links3) {
								String href3 = checkExtAndDownload(link3, 3);
							}
						}
					}
				}
			}
		}
	}

	public String addHost(String href, String host){
		String firstLetter = String.valueOf(href.charAt(0));

		if( !firstLetter.equals("h")){
			if (firstLetter.equals("/")){
				href = host.substring(0,host.length()-1) + href;
			}else if( href.substring(0,3).equals("www")){
				href = "http://" + URL;
			}else{
				href = host + href;
			}
		}
		return href;
	}

	public String checkExtAndDownload(Element link, int page){
		String href = link.attr("href");
		href = addHost(href, URL);
		String ext1 = FilenameUtils.getExtension(href);
		if(ext1.equals("mid")){

			downloadMidi(link);
			System.out.println( page + ": " + href);
		}
		return href;
	}

	public boolean checkUrl(String url) throws MalformedURLException{

		if(url.length()<7){
			return false;
		}
		if( !url.substring(0,7).equals("http://")){
			return false;
		}
		URL url2 = new URL(url);
		HttpURLConnection huc;
		try {
			huc = (HttpURLConnection) url2.openConnection();
			int responseCode = huc.getResponseCode();
			//System.out.println(responseCode);
			if (responseCode == 200 || 
					responseCode == 202 ||
					responseCode == 302 ||
					responseCode == 403){
				return true;
			}else{
				return false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;


	}

	public Elements getLinks(String url) throws IOException{

		if(checkUrl(url)){
			Document doc = Jsoup.connect(url)
					.userAgent(USER_AGENT)
					.get();
			Elements links = doc.select("a[href]");
			return links;
		}else{
			return null;
		}

	}

	public void downloadMidi(Element link){
		String href = link.attr("href");
		String song = link.text();
		try{
			URL mURL = new URL(href);
			String pathname = MIDI_DIRECTORY + song + ".mid";
			File file = new File(pathname);

			FileUtils.copyURLToFile(mURL, file);
		}catch(Exception e){
			System.err.println("Error: " + e.getMessage());
		}

	}

	public static void main(String[] args) throws IOException{

		new MidiDownloader();
	}

}


