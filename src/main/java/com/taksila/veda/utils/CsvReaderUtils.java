package com.taksila.veda.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;

import com.csvreader.CsvReader;

public class CsvReaderUtils {
	public static void main(String[] args) {
		URL url = CsvReaderUtils.class.getClassLoader().getResource("CouponCodes/CouponCodes.csv");
	    System.out.println(url.getPath());
		String fileName = url.getPath();
		
		if(fileName != null && !fileName.isEmpty()) {
			CsvReaderUtils csvReader = new CsvReaderUtils();
			System.out.println("CSV Reader");
			csvReader.ReadCSVData(fileName);
		}
	}

	private void ReadCSVData(String fileName) {
		CsvReader csvReader = null;
		try {
			csvReader = new CsvReader(new FileReader(fileName));
			String[] nextLine;
//			CouponInfoDAO couponInfoDAO = new CouponInfoDAO();
			
			while(csvReader.readRecord()) {
				nextLine = csvReader.getValues();
				for (String string : nextLine) { 
//					Coupon coupon = new Coupon();
//					coupon.setCouponCode(string);
//					coupon.setExpiryDate("2016-10-31");
//					coupon.setPlanId("P2000");
//					coupon.setAccountId("");
//					coupon.setIsCouponValid("N");
//					couponInfoDAO.createCoupon(coupon);
				}
			} 
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
