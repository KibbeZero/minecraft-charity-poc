package com.kibbezero.extralife.donordriveclient;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

public class Connection {

    private static final String API_VERSION = "1.1";

    //region Static URIs

    private static final String TEAM_PARTICIPANTS_URI = "api/teams/%s/participants"; //Format with team ID. For ExtraLife2020 our ID is 50922
    private static final String PARTICIPANT_URI = "api/participants/%s"; //Format with a participant ID. KibbeZero's participant ID is 403971
    private static final String PARTICIPANT_DONATIONS_URI = "api/participants/%s/donations";
    //endregion

    //region Static Parameters for Array-based Endpoints
    private static final String ORDER_BY_PARAMETER = "&orderBy=%s";
    private static final String VERSION_PARAMETER = "&version=" + API_VERSION; //Please put this in all generated URIs for consistency
    private static final String LIMIT_PARAMETER = "&limit=%s"; // Let's all be good stewards of the API and use this liberally
    private static final String WHERE_PARAMETER = "&where=%s"; // Uses "SQL-style" rules. See https://github.com/DonorDrive/PublicAPI/tree/master/docs/1.1
    //endregion


    private URL donorURL; // Test Data is https://try.donordrive.com ; Extra Life is https://extralife.donordrive.com
    private void setDonorURL(String baseURL) throws MalformedURLException {
        donorURL = new URL(baseURL);
    }
    public URL getDonorSite() { return donorURL; }

    public Connection(String donorURL) {
        try {
            setDonorURL(donorURL);
        } catch(MalformedURLException exception) {
            throw new IllegalArgumentException("donorURL is not a valid URL. Example of valid URL: https://try.donordrive.com", exception);
        }
    }

    public Participant getParticipant(String participantId) throws IOException {

        URL participantURL = new URL(getDonorSite(), String.format(PARTICIPANT_URI, participantId));
        try {
            Reader reader = new InputStreamReader(participantURL.openStream());
            return new Gson().fromJson(reader, Participant.class);
        } catch (IOException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new IOException("Something went wrong parsing Json response. You may have a bad URL (Extralife just redirects if it finds a malformed URL)", exception);
        }
    }


}
