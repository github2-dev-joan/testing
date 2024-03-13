package com.luv2code.WebScraperDB1.restcontroller;

import com.luv2code.WebScraperDB1.CustomResponse;
import com.luv2code.WebScraperDB1.entity.Actual;
import com.luv2code.WebScraperDB1.entity.History;
import com.luv2code.WebScraperDB1.entity.Logs;
import com.luv2code.WebScraperDB1.service.ActualServiceImpl;
import com.luv2code.WebScraperDB1.service.HistoryServiceImpl;
import com.luv2code.WebScraperDB1.service.LogsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/tregjet")
public class RestController {
    @Autowired
    private ActualServiceImpl actualServiceImpl;

    @Autowired
    private HistoryServiceImpl historyServiceImpl;

    @Autowired
    private LogsServiceImpl logsServiceImpl;

    public RestController(ActualServiceImpl actualServiceImpl, HistoryServiceImpl historyServiceImpl, LogsServiceImpl logsServiceImpl) {
        this.actualServiceImpl = actualServiceImpl;
        this.historyServiceImpl = historyServiceImpl;
        this.logsServiceImpl = logsServiceImpl;
    }

    @GetMapping("/checkRevNo")
    public CustomResponse<Object> checkRevisionNumber(@RequestParam String revisionNo) {
        try {
            long revNoLong= Long.valueOf(revisionNo);
            //System.out.println("revision no " + revisionNo);
            List<Actual> storeExistingTable = actualServiceImpl.getAllActual();

            Logs insideCheckRevNo = new Logs("success", "DEBUG", "Inside checkRevNo");
            logsServiceImpl.addLogsToDB(insideCheckRevNo);

            //System.out.println("2");

            CustomResponse<Object> response;

            if (storeExistingTable != null && !storeExistingTable.isEmpty()) {

                insideCheckRevNo = new Logs("success","DEBUG","Existing table is populated");
                logsServiceImpl.addLogsToDB(insideCheckRevNo);

                //System.out.println("3");
                if (storeExistingTable.get(0).getRevisionNo() > revNoLong) {
                    //po ka kurse me revision number me te larte
                    insideCheckRevNo = new Logs("success","INFO","There are new rates");
                    logsServiceImpl.addLogsToDB(insideCheckRevNo);
                    response = new CustomResponse<>(true, "There are new rates");
                    return response;
                    //return true;
                }
            }
            insideCheckRevNo = new Logs("success","INFO","The rates are up to date");
            logsServiceImpl.addLogsToDB(insideCheckRevNo);
            response = new CustomResponse<>(false, "The rates are up to date");
            //return false;

        }catch (NumberFormatException n){
            CustomResponse<Object> parsingResponse = new CustomResponse<>(false, "parsing of revision no failed");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        Logs exception = new Logs("failed", "WARN","checkRevisionNo method failed");
        logsServiceImpl.addLogsToDB(exception);
        CustomResponse<Object> failResponse = new CustomResponse<>(false, "checkRevisionNo method failed, make sure you have entered a long value to revNo ");
        return failResponse;
        //return false;

    }

    //@CrossOrigin(origins = "http://127.0.0.1:5500", methods = {RequestMethod.GET})
    @GetMapping("/getActualTable")
    public List<Actual> getActualTable() {
        Logs returningActual = new Logs("success", "INFO", "Returning the actual table");
        logsServiceImpl.addLogsToDB(returningActual);
        return actualServiceImpl.getAllActual();

    }

    @GetMapping("/getHistoryTableByDate")
    public CustomResponse<History> getHistoryTableByDate(@RequestParam String date) {
        //String fullDateStr = year + "-" + month + "-" + day;
        String format = "yyyy-MM-dd";
        LocalDate formattedDate = historyServiceImpl.formatStringToDate(date, format);
        System.out.println(formattedDate);

        Logs returningHistory = new Logs("success", "INFO", "Returing the history table by date");
        logsServiceImpl.addLogsToDB(returningHistory);

        List<History> historyList = historyServiceImpl.getHistoryByDate(formattedDate);
        System.out.println(historyList);
        CustomResponse<History> response;

        if(historyList!=null && !historyList.isEmpty()){
            //happy path
            //return historyServiceImpl.getHistoryByDate(formattedDate);
            response = new CustomResponse<>(true, "History table found", historyList);
            return response;

        }
        //tabela eshte bosh kthe nje custom error message
        response = new CustomResponse<>(false,"History table not found ", null);
        return response;

    }

    @GetMapping("/getHistoryTableByRevNo")
    public CustomResponse<History> getHistoryTableByRevNo(@RequestParam long revisionNo) {
        Logs returningHistory = new Logs("success", "INFO", "Returning the history table by revisionNo");
        logsServiceImpl.addLogsToDB(returningHistory);
        List<History> historyList = historyServiceImpl.getHistoryByRevNo(revisionNo);
        CustomResponse<History> response;
        if(historyServiceImpl.getHistoryByRevNo(revisionNo)!=null && !historyServiceImpl.getHistoryByRevNo(revisionNo).isEmpty()){
            //happy path
            //return historyServiceImpl.getHistoryByRevNo(revisionNo);
            response = new CustomResponse<>(true, "History table found", historyList);
            return response;
        }
        //tabela eshte bosh kthe nje custom error message
        response = new CustomResponse<>(false,"History table not found ", null);
        return response;
    }


}
