package br.com.delxmobile.fidelidade.service.mappers.program;

import java.util.HashMap;
import java.util.Map;
import br.com.delxmobile.fidelidade.db.mappers.Mapper;
import br.com.delxmobile.fidelidade.model.Program;

/**
 * Created by Guilherme on 26/12/2017.
 */

public class ProgramToMap implements Mapper<Program, Map<String, Object>> {
    @Override
    public Map<String, Object> map(Program program) {
        Map<String, Object> map = new HashMap<>();
        map.put("updated_at", program.updatedAt);
        map.put("user_oid", program.userOid);
        map.put("points", program.points);
        map.put("name", program.name);
        map.put("description", program.description);
        return map;
    }
}
