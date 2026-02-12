package ir.joorjens.joorapp.models;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by meysammoh on 05.05.18.
 */

public class OrderStatusController {
    private TreeMap<Long, String> orderStatusMap;

    private static OrderStatusController _instance = null;
    public static OrderStatusController getInstance(){
        if(_instance==null)
            _instance = new OrderStatusController();
        return _instance;
    }
    private OrderStatusController(){

    }
    public void initializeOrderStatus(List<PairResultItem> statusPairs){
        orderStatusMap = new TreeMap();
        for(int i = 0 ; i< statusPairs.size(); i++){
            orderStatusMap.put( statusPairs.get(i).getFirst(), statusPairs.get(i).getSecond());
        }
    }

    public boolean canBeDelivered( int currentStateId ) throws NullPointerException{
        if(orderStatusMap.lowerKey( orderStatusMap.lastKey() ) == currentStateId)
            return true;
        return false;
    }

    public Long getDeliveredState(){
        return orderStatusMap.lastKey();
    }

}
