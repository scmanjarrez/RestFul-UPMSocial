package rest;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import rest.post.Post;
import rest.post.PostsList;
import rest.user.User;

public class prueba {
	public static Connection connectToDB()
			throws ClassNotFoundException, SQLException {

		Class.forName("com.mysql.jdbc.Driver");
		//String url= "jdbc:mysql://db4free.net:3306/UPMSocial";
		String url= "jdbc:mysql://localhost:3306/UPMSocial";
		Connection conexion= DriverManager.getConnection(url, "upmsocial", "upmsocial");
		return conexion;	
	}
	public static String obtenerMetadatos(Connection conexion, String tabla)
			throws SQLException {
		System.out.println("Estoy en metadatos");
		String resultado=null;	
		String nResultado=null;
		Statement sentencia = conexion.createStatement();
		ResultSet rsTabla = sentencia.executeQuery("select * from "+tabla);
		ResultSetMetaData metaD = rsTabla.getMetaData();
		
		int NCol = metaD.getColumnCount();
		for(int i=1; i <= NCol;i++){
			if (i == 1){
				resultado = metaD.getTableName(i)+ ";"+ NCol + ";";
				DatabaseMetaData meta = conexion.getMetaData();

				ResultSet rs = meta.getPrimaryKeys(null, null, tabla); // Con esto y el bucle, obtenemos las PrimaryKeys
				while (rs.next()) {
					String columnName = rs.getString("COLUMN_NAME");
					resultado = resultado + columnName + ",";
				}
				
				nResultado = resultado.substring(0,resultado.length()-1)+';'; // Cambiamos la ',' por un ';' para mantener la estructura
				nResultado = nResultado + metaD.getColumnLabel(i) + ","+ metaD.getColumnTypeName(i) + ","+ metaD.getColumnDisplaySize(i);
				
			}else if(i == NCol){
				nResultado = nResultado +  ";" + metaD.getColumnLabel(i) + ","+ metaD.getColumnTypeName(i) + ","+ metaD.getColumnDisplaySize(i);
			}else{
				nResultado = nResultado +  ";" + metaD.getColumnLabel(i) + ","+ metaD.getColumnTypeName(i) + ","+ metaD.getColumnDisplaySize(i);
			}

		}
		return nResultado;
	}
	
	public static ArrayList<User> obtenerUsuarios(Connection conexion)
			throws SQLException {
		User user;	
		ArrayList<User> u_list = new ArrayList<User>();
		Statement sentencia = conexion.createStatement();
		ResultSet rsTabla = sentencia.executeQuery("select * from USERS");
		
		while(rsTabla.next()){
			user = new User();
			user.setUsername(rsTabla.getString("username"));
			user.setFirst_name(rsTabla.getString("first_name"));
			user.setLast_name(rsTabla.getString("last_name"));
			user.setPhone((Integer)rsTabla.getObject("phone"));
			user.setEmail(rsTabla.getString("email"));
			user.setAddress(rsTabla.getString("address"));
			user.setRegister_date(rsTabla.getDate("register_date"));
			u_list.add(user);
		}
		return u_list;
	}
	
	public static void main(String[] args) {
		/*Connection nConexion = null;
		String metaDatos = null;
		ArrayList<User> u_list = null;
		try {
			nConexion = connectToDB();
			//metaDatos = obtenerMetadatos(nConexion, "USERS");
			u_list = obtenerUsuarios(nConexion);
			System.out.println(u_list.toString());

		} catch (ClassNotFoundException | SQLException  e) {
			e.printStackTrace();
		}finally{
			try {
				nConexion.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}*/
		/*User usuario = new User();
		usuario.setAddress("Calle Hola Mundo 2");
		usuario.setUsername("testUN");
		usuario.setFirst_name("UsuarioPrueba");
		//usuario.setPhone(910212354);
		//usuario.setPhone(null);
		Date date = new Date();
		usuario.setRegister_date(date);
		
		try {
            JAXBContext context = JAXBContext.newInstance(User.class);

            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(usuario, System.out);
        } catch (JAXBException e) {
            e.printStackTrace();
        }*/
		
		Date date = new Date();
		
		PostsList p_list = new PostsList();
		Post post = new Post();
		post.setAuthor_username("lorca");
		post.setContent("tasdasdasd asdasdasd estUN");
		post.setId(1);
		post.setCreation_date(date);
		Post post1 = new Post();
		post1.setAuthor_username("lorca");
		post1.setContent("tasdasdasd asdasdasd estUN");
		post1.setId(2);
		post1.setCreation_date(date);
		
		ArrayList<Post> pl = new ArrayList<Post>();
		pl.add(post);
		pl.add(post1);
		p_list.setP_list(pl);
		
		try {
            JAXBContext context = JAXBContext.newInstance(PostsList.class);

            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(p_list, System.out);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
		
	}

}
