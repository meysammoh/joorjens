package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.clazz.Third;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.VehicleRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.Vehicle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VehicleRepositoryImpl
        extends RepositoryImpAbstract<Long, Vehicle>
        implements VehicleRepository {

    @Override
    @SuppressWarnings("unchecked")
    public FetchResult<Vehicle> search(long id
            , int licensePlateFirst, String licensePlateLetter, int licensePlateSecond, int licensePlateCode
            , int color, int manufactureYear, long vehicleBrandTypeId, long distributorId
            , Boolean block, int max, int offset) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id
                , licensePlateFirst, licensePlateLetter, licensePlateSecond, licensePlateCode
                , color, manufactureYear, vehicleBrandTypeId, distributorId, block, false);
        return search(query.getFirst(), query.getSecond(), max, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Third<Long, String, String>> getAllPairs(long id
            , int licensePlateFirst, String licensePlateLetter, int licensePlateSecond, int licensePlateCode
            , int color, int manufactureYear, long vehicleBrandTypeId, long distributorId, Boolean block) throws JoorJensException {
        Pair<String, Map<String, Object>> query = getQuery(id
                , licensePlateFirst, licensePlateLetter, licensePlateSecond, licensePlateCode
                , color, manufactureYear, vehicleBrandTypeId, distributorId, block, true);
        List<Object[]> allBy = findAllBy(query.getFirst(), query.getSecond());
        return allBy.stream()
                .map(obj -> new Third<>((long) obj[0], (String) obj[1], (String) obj[2]))
                .collect(Collectors.toList());
    }

    private Pair<String, Map<String, Object>> getQuery(long id
            , int licensePlateFirst, String licensePlateLetter, int licensePlateSecond, int licensePlateCode
            , int color, int manufactureYear, long vehicleBrandTypeId, long distributorId, Boolean block, boolean pair) {
        final StringBuilder query = new StringBuilder();
        final Map<String, Object> map = new HashMap<>();
        if (pair) {
            query.append("SELECT t.id,t.licensePlate,t.vehicleBrandType.name");
        } else {
            query.append("SELECT %s");
        }
        query.append(" FROM Vehicle t WHERE 1=1");
        addCommonQuery(query, map, id, block);
        if (licensePlateFirst > 0 && !Utility.isEmpty(licensePlateLetter)) {
            final String licensePlate = Vehicle.getLicensePlate(licensePlateFirst, licensePlateLetter, licensePlateSecond, licensePlateCode);
            query.append(" and t.licensePlate=:licensePlate");
            map.put("licensePlate", licensePlate);
        }
        if (color > 0) {
            query.append(" and t.color=:color");
            map.put("color", color);
        }
        if (manufactureYear > 0) {
            query.append(" and t.manufactureYear=:manufactureYear");
            map.put("manufactureYear", manufactureYear);
        }
        if (vehicleBrandTypeId > 0) {
            query.append(" and t.vehicleBrandType.id=:vehicleBrandTypeId");
            map.put("vehicleBrandTypeId", vehicleBrandTypeId);
        }
        if (distributorId > 0) {
            query.append(" and t.distributor.id=:distributorId");
            map.put("distributorId", distributorId);
        }

        if (block != null) {
            query.append(" and t.vehicleBrandType.block=:vehicleBlock");
            query.append(" and t.distributor.block=:disBlock");
            map.put("vehicleBlock", block);
            map.put("disBlock", block);
        }

        return new Pair<>(query.toString(), map);
    }
}