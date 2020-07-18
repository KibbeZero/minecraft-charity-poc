package com.kibbezero.extralife.donordriveclient;

import java.io.IOException;

public interface IConnection {
    Participant[] getTeamParticipants(String teamId) throws IOException;
    Participant getParticipant(String participantId) throws IOException;
    Donation[] getParticipantDonations(String participantId) throws IOException;
    //Incentive[] getParticipantIncentives(String participantId) throws IOException;
}
