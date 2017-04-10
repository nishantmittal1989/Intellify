package com.amex.intellify.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.amex.intellify.model.Amex;
import com.amex.intellify.model.Bofa;
import com.amex.intellify.model.Card;
import com.amex.intellify.model.CardDetails;
import com.amex.intellify.model.Cards;
import com.amex.intellify.model.Chase;
import com.amex.intellify.model.Citi;
import com.amex.intellify.model.Discover;
import com.amex.intellify.model.RegisterUser;
import com.amex.intellify.model.SwipeInfo;
import com.amex.intellify.model.SwipeInfoResponse;
import com.google.gson.Gson;

public class DbHelper {

	private final static Logger logger = Logger.getLogger(DbHelper.class);
	
	
	public static void main(String[] args) {
		Gson gson = new Gson();
		DbHelper helper = new DbHelper();
		/*SwipeInfoResponse re = helper.getIntellifyStats();
		String res = gson.toJson(re);
		System.out.println("Response: "+res);*/
		
		
		SwipeInfo info = new SwipeInfo();
		info.setCardType("citi");
		info.setMerchantId("2");
		String response = helper.setSwipeInfo(info);
		System.out.println(response);
		
	}
	
	public SwipeInfoResponse getIntellifyStats(){
		
		Connection con = null;
		PreparedStatement psGetIntellifyStats = null;
		DbHelper helper = new DbHelper();
		
		try {
			con = helper.dbconnection();

			String getIntellifyStats = "SELECT count(*) as stat, \"MERCHANT\".\"Category\", \"CARD\".\"Type\" FROM intellify.\"TRANSACTION\" join  intellify.\"CARD\" "
					+ "on \"CARD\".card_number = \"TRANSACTION\".card_id join intellify.\"MERCHANT\" on \"MERCHANT\".merchant_id = \"TRANSACTION\".merchent_id "
					+ "group by \"MERCHANT\".\"Category\",\"CARD\".\"Type\" Order by \"CARD\".\"Type\",\"MERCHANT\".\"Category\";";
			psGetIntellifyStats = con.prepareStatement(getIntellifyStats);
			System.out.println("query:" +getIntellifyStats);
			ResultSet rs = psGetIntellifyStats.executeQuery();
			SwipeInfoResponse res = new SwipeInfoResponse();
			Cards cards = new Cards();
			CardDetails detailsAmex = new CardDetails();
			CardDetails detailsCiti = new CardDetails();
			CardDetails detailsChase = new CardDetails();
			CardDetails detailsBofa = new CardDetails();
			CardDetails detailsDiscover = new CardDetails();
			
			Card cardAmex = new Amex();
			Card cardChase = new Chase();
			Card cardCiti = new Citi();
			Card cardBofa = new Bofa();
			Card cardDiscover = new Discover();
			
			while (rs.next()) {
					if(rs.getString("Type").trim().equalsIgnoreCase("amex")){
						cardAmex  = setCardProperties(rs,cardAmex);
					}else if(rs.getString("Type").trim().equalsIgnoreCase("bofa")){
						cardBofa = setCardProperties(rs,cardBofa);
					}else if(rs.getString("Type").trim().equalsIgnoreCase("chase")){
						cardChase = setCardProperties(rs,cardChase);
					}else if(rs.getString("Type").trim().equalsIgnoreCase("citi")){
						cardCiti = setCardProperties(rs,cardCiti);
					}else if(rs.getString("Type").trim().equalsIgnoreCase("discover")){
						cardDiscover = setCardProperties(rs,cardDiscover);
					}
				}
			
			detailsDiscover.setDiscover((Discover)cardDiscover);
			cards.add(detailsDiscover);
			
			detailsCiti.setCiti((Citi)cardCiti);
			cards.add(detailsCiti);
			
			detailsAmex.setAmex((Amex)cardAmex);
			cards.add(detailsAmex);
			
			detailsChase.setChase((Chase)cardChase);
			cards.add(detailsChase);
			
			detailsBofa.setBofa((Bofa)cardBofa);
			cards.add(detailsBofa);
			
			
			res.setCards(cards);
			return res;
			
		}catch(Exception Ex){
			
			System.out.println("error: "+Ex.getMessage());
			return null;
		}finally{
			try {
				if(!con.isClosed()){
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Card setCardProperties(ResultSet rs, Card card) throws SQLException{
		//check the code from here, there are some issues in the code, fix it in the morning.
		
		if(rs.getString("Category").trim().equalsIgnoreCase("clothing")){
		card.setClothing(rs.getInt("stat"));
		}else if(rs.getString("Category").trim().equalsIgnoreCase("electronics")){
			card.setElectronics(rs.getInt("stat"));
		}else if(rs.getString("Category").trim().equalsIgnoreCase("gas")){
			card.setGas(rs.getInt("stat"));
		}else if(rs.getString("Category").trim().equalsIgnoreCase("restaurants")){
			card.setRestaurants(rs.getInt("stat"));
		}else if(rs.getString("Category").trim().equalsIgnoreCase("retail")){
			card.setRetail(rs.getInt("stat"));
		}
		
		return card;
	}
	
	public String setSwipeInfo(SwipeInfo info) {

		Connection con = null;
		PreparedStatement psGetCardId = null;
		PreparedStatement psInsertTransaction = null;
		DbHelper helper = new DbHelper();
		
		try {
			con = helper.dbconnection();

			String getCardId = "select card_number,cust_id from intellify.\"CARD\" where \"Type\" = '"+info.getCardType()+"';";
			System.out.println("select query: "+getCardId);
			psGetCardId = con.prepareStatement(getCardId);
			ResultSet rs = psGetCardId.executeQuery();

			while (rs.next()) {
				int card_id = rs.getInt("card_number");
				System.out.println(card_id);
				String insertTransaction = "insert into intellify.\"TRANSACTION\"(card_id, merchent_id) values ('" + card_id + "','"
						+ info.getMerchantId() + "');";
				System.out.println("insert query:"+insertTransaction);
				psInsertTransaction = con.prepareStatement(insertTransaction);
				int retcode = psInsertTransaction.executeUpdate();
				if (retcode != 0) {
					return "Success";
				} else {
					return "Failure";
				}

			}

		} catch (Exception ex) {
			logger.error("setUser:Dbhelper.java have errors: " + ex.getMessage());
		}finally{
			try {
				if(!con.isClosed()){
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return "Card does not exist. This User is not an Intellify Customer";
	}

	public Connection dbconnection() {
		// TODO Auto-generated method stub
		Connection con = null;

		try {
			Class.forName("org.postgresql.Driver");
			
			con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/intellify", "postgres", "12345");
		} catch (ClassNotFoundException e1) {
			System.out.println("ClassNotFoundException");
			e1.printStackTrace();
		} catch (SQLException e) {
			System.out.println("SQLException");
			e.printStackTrace();
		}

		return con;
	}

	public String validateUser(RegisterUser registerUser) {
		
		Connection con = null;
		PreparedStatement psGetIntellifyKey = null;
		DbHelper helper = new DbHelper();
		
		try {
			con = helper.dbconnection();

			String getIntellifyKey = "select intellify_key,cust_id from intellify.\"INTELLIFYKEY\" where \"intellify_key\" = '"+registerUser.getIntellifyKey()+"';";
			//System.out.println("select query: "+getCardId);
			psGetIntellifyKey = con.prepareStatement(getIntellifyKey);
			ResultSet rs = psGetIntellifyKey.executeQuery();

			while (rs.next()) {
				String intellify_key = rs.getString("intellify_key");
				String customer_id = rs.getString("cust_id");
				System.out.println(intellify_key);
				
				if(intellify_key.equalsIgnoreCase(registerUser.getIntellifyKey())){
					return "customer_id";
				}
				else{
					return "Failure";
				}
			}
			
		}catch(Exception ex){
			return "Failure";
		}finally{
			try {
				if(!con.isClosed()){
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return "Failure";
	}

	public String createUser(RegisterUser registerUser, String customerId) {
		
		Connection con = null;
		PreparedStatement psInsertTransaction = null;
		DbHelper helper = new DbHelper();
		
		try{
			
			con = helper.dbconnection();
			String  insertTransaction ="INSERT INTO intellify.\"CREDENTIAL\" ( username, password, cust_id) values "
					+ "('"+registerUser.getUsername()+"', '"+registerUser.getPassword()+"', '"+customerId+"');";
			
			psInsertTransaction = con.prepareStatement(insertTransaction);
			int retcode = psInsertTransaction.executeUpdate();
			if (retcode != 0) {
				return "Success";
			} else {
				return "Failure";
			}
				
		}catch(Exception ex){
			
		}finally{
			try {
				if(!con.isClosed()){
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
