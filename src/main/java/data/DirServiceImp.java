package data;

import java.sql.*;
import dataService.Dirservice;

public class DirServiceImp implements Dirservice {

	@Override
	public void saveDirToSQL(double[][] dir) {
		// TODO Auto-generated method stub
		Statement stmt=DatabaseController.getStmt();
		for(int i=0;i<dir.length;i++){
			String sql="insert into dir values('";
			for(int j=0;j<dir[0].length;j++){
				sql=sql+dir[i][j]+" ,";
			}
			sql=sql+"');";
			try {
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		DatabaseController.commit();
	}

}
