package br.com.delxmobile.fidelidade.service.mappers.user;

import java.util.HashMap;
import java.util.Map;
import br.com.delxmobile.fidelidade.db.mappers.Mapper;
import br.com.delxmobile.fidelidade.model.User;
/**
 * Created by Guilherme on 26/12/2017.
 */
public class UserToMap implements Mapper<User, Map<String, Object>> {

    @Override
    public Map<String, Object> map(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("oid", user.oId);
        map.put("updated_at", user.updatedAt);
        map.put("photo", user.photo);
        map.put("name", user.name);
        map.put("email", user.email);
        return map;
    }
}
