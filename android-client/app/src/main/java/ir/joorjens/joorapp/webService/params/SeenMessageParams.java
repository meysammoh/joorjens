package ir.joorjens.joorapp.webService.params;

import android.util.Pair;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SeenMessageParams {
    //private List<List<Integer>> mMessagesStatus;
    private Integer[][] mMessagesStatus;

    public SeenMessageParams(List<Pair<Integer,Integer>> messagesIdList) {
        mMessagesStatus = new Integer[messagesIdList.size()][];
        int c = 0;
        for(Pair<Integer,Integer> msgIdmsgRcvId : messagesIdList){
            mMessagesStatus[c] = new Integer[2];
            mMessagesStatus[c][0] = msgIdmsgRcvId.first;
            mMessagesStatus[c][1] = msgIdmsgRcvId.second;
            c++;
        }

//        this.mMessagesStatus = new ArrayList<>();
//        for(Integer msgId : messagesIdList){
//
//            List<Integer> tmp = new ArrayList<>();
//            tmp.add(myId);
//            tmp.add(msgId);
//
//            mMessagesStatus.add(tmp);
//        }
    }

    public String toString(){
        Gson gson = new Gson();
        String json = gson.toJson(mMessagesStatus);
        return json;
    }
}
