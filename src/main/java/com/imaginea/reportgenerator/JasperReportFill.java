package com.imaginea.reportgenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.imaginea.bandwidth.usage.model.SRXLogSummary;
import com.imaginea.callmanager.model.MasterRecord;

public class JasperReportFill {
	public static void main(String[] args) {
		String sourceFileName ="";
		sourceFileName = "src/main/resources/MasterReport.jasper";

		CallManagerRestAPICall apiCall = new CallManagerRestAPICall();
		MasterRecord masterRecord = apiCall.generateReport();
		
		SRXRestAPICall srxRestAPICall = new SRXRestAPICall();
		SRXLogSummary srxLogSummary = srxRestAPICall.getSRXLogSummary();
		masterRecord.setMostIPHit(srxLogSummary.getMostHitIP_Log());
		masterRecord.setLanDeviceRequests(srxLogSummary.getLanDeviceLog());
		masterRecord.setWirelessDeviceRequest(srxLogSummary.getWirelessDeviceLog());
		
		
		ArrayList<MasterRecord> beanList = new ArrayList<MasterRecord>();
		beanList.add(masterRecord);

		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(beanList);

		Map parameters = new HashMap();

		try {
			String printFileName = JasperFillManager.fillReportToFile(
					sourceFileName, parameters, beanColDataSource);
			if (printFileName != null) {
				JasperExportManager.exportReportToPdfFile(printFileName,
						"D://CallRecords.pdf");
			}
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
}
