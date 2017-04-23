package data;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import dataService.WordService;

public class WordServiceImp implements WordService{

	@Override
	public void saveWordToSQL(HashMap<String, double[]> words) {
		// TODO Auto-generated method stub
		Statement stmt=DatabaseController.getStmt();
		Iterator<?> iter = words.entrySet().iterator();
		int count=0;
		while(iter.hasNext()){
			Map.Entry entry = (Map.Entry) iter.next();
			String key=(String) entry.getKey();
			String sql="insert into words values('"+key+"','";
			double[] val = (double[]) entry.getValue();
			for(int i=0;i<val.length;i++){
				sql=sql+val[i]+",";
			}
			sql=sql+"');";
			try {
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			count++;
			if(count==1000){
				DatabaseController.commit();
				count=0;
			}
		}
		DatabaseController.commit();
	}

}
