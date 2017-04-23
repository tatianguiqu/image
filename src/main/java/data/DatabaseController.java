package data;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import dataService.Dirservice;
import dataService.WordService;
import image.image.ImageKey;
public class DatabaseController {
	private static  Connection conn;
	private static  Statement stmt;
	static{

		 try{
	           Class.forName("com.mysql.jdbc.Driver");
	        }catch(ClassNotFoundException e){
	            e.printStackTrace();
	        }
		  String url="jdbc:mysql://localhost:3306/imagesearch";    //JDBC的URL   
		  try {
			conn = DriverManager.getConnection(url,"root",Path.secret);
			conn.setAutoCommit(false);
			stmt=conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		  
		
	}
	public static void main(String[] args){
		Dirservice d=new DirServiceImp();
		WordService w=new WordServiceImp();
		d.saveDirToSQL(new double[][]{{1,2,3}});
		HashMap<String, double[]> words=new HashMap<String, double[]>();
		words.put("asad", new double[]{1,23});
		w.saveWordToSQL(words);
		close();
	}
	public static Statement getStmt(){
		return stmt;
	}
	public static Statement getNewStmt(){
		 Statement newStmt = null;
		 try {
			  newStmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return newStmt;
	}

	public static void commit(){
		try{   
			conn.commit();  
        }catch(Throwable e1){  
            if(conn!=null){  
                 try {  
                     conn.rollback();  //数据库回滚
                } catch (SQLException e2) {  
                    e2.printStackTrace();  
                }  
            }  
            throw new RuntimeException(e1);  
          }

	}
	public void saveImageKey(ImageKey[] list){
		  try{  
			      for(int i=0;i<list.length;i++){
			    	  stmt.executeUpdate(makeSql(list[i],"imagekeyT"));
			    	  if (i%100==0){
			    		  conn.commit();   
			    	  }
			      }
			          
			
			          conn.commit();  
			         }catch(Throwable e1){  
			            if(conn!=null){  
			                 try {  
			                     conn.rollback();  //数据库回滚
			                } catch (SQLException e2) {  
			                    e2.printStackTrace();  
			                }  
			            }  
			    
			  throw new RuntimeException(e1);  
			          }

	}
	public static ArrayList<ImageKey> getImageKey(String tableName){
		ArrayList<ImageKey> ikList=new ArrayList<ImageKey>();
		
		String sql="select * from "+tableName;
		try {
			ResultSet rs=stmt.executeQuery(sql);
			
			
			while(rs.next()){
				ImageKey ik=new ImageKey();
				double[] imageKey=new double[30];
				ik.setPath(rs.getString(1));
				ik.setC(rs.getString(2));
				for(int i=0;i<30;i++){
					imageKey[i]=rs.getDouble(i+3);
				}
			    ik.setImageKey(imageKey);
			    ikList.add(ik);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ikList;
	}
	public static void close(){
		if(stmt!=null){
			try {
				stmt.close();
				 conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	       
		}
	
	}
	private  static String makeSql(ImageKey ik,String tableName){
		String result="insert into "+tableName+" values(";
		result=result+"'"+ik.getPath()+"' ,"+ik.getC();
		double[] imageKey=ik.getImageKey();
		for(int i=0;i<imageKey.length;i++){
			result=result+","+imageKey[i];
		}
		result=result+");";
		
		return result;
	}
}
