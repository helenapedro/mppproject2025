package edu.miu.eems.dao.assoc;

import edu.miu.eems.dao.Dao; import edu.miu.eems.domain.EmployeeProject; import java.sql.*; import java.util.*;

public class FindAllocationsByProjectDao implements Dao<EmployeeProject> {
    private final int projectId;
    private final List<EmployeeProject> out = new ArrayList<>();

    public FindAllocationsByProjectDao(int projectId){
        this.projectId = projectId;
    }

    @Override
    public String getSql(){
        return "SELECT * FROM employee_project WHERE project_id=?";
    }

    @Override
    public void bind(PreparedStatement ps) throws SQLException {
        ps.setInt(1, projectId);
    }

    @Override
    public void unpack(ResultSet rs) throws SQLException {
        while(rs.next())
            out.add(new EmployeeProject(
                    rs.getInt("employee_id"),
                    rs.getInt("project_id"),
                    rs.getDouble("allocation_pct"))
            );
    }

    @Override
    public List<EmployeeProject> getResults(){
        return out;
    }
}
