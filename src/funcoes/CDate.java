/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package funcoes;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author User
 */
public class CDate {

    /**
     *
     */
    public CDate() {
    }

    public String DataMySQLtoDataStringPT(String data_) {
        DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date data = null;
        try {
            data = (Date) f.parse(data_);
        } catch (ParseException ex) {
            System.err.println("Erro ao converter data :" + ex);
        }
        return sdf.format(data);
    }

    public String DataPTBRAtual() {
        SimpleDateFormat databr = new SimpleDateFormat("dd/MM/yyyy");
        String atual = databr.format(new Date());
        return atual;
    }

    public String DataPTBRtoDataMySQL(String temp) {
        DateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date data = null;
        try {
            data = (Date) f.parse(temp);
        } catch (ParseException ex) {
            System.err.println("Erro ao converter data :" + ex);
        }
        String temp2 = sdf.format(data);
        return temp2;
    }

    public String SomarData(int dias, String dataInicial_) throws Exception {
        try {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            Date dataInicial = formato.parse(dataInicial_);
            Calendar calendarData = Calendar.getInstance();
            calendarData.setTime(dataInicial);
            calendarData.add(Calendar.DATE, dias);
            Date dataSomada = calendarData.getTime();
            return formato.format(dataSomada);
        } catch (Exception ex) {
            throw new Exception("Erro ao converter data. " + ex);
        }
    }

    public long diasRestantes(String dataInicio) throws Exception {
        int dias = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar data1 = Calendar.getInstance();
        Calendar data2 = Calendar.getInstance();
        try {
            data2.setTime(sdf.parse(dataInicio));
        } catch (java.text.ParseException e) {
        }
        if (data1.get(Calendar.YEAR) == data2.get(Calendar.YEAR)){
            dias = data2.get(Calendar.DAY_OF_YEAR)- data1.get(Calendar.DAY_OF_YEAR);
        }
        else if (data1.get(Calendar.YEAR) < data2.get(Calendar.YEAR)){
            int bix=0;
            if (data1.get(Calendar.YEAR)%4==0 && data1.get(Calendar.YEAR)%100!=0 && data1.get(Calendar.YEAR)%400==0)
                bix = 366;
            else
                bix = 365;
            dias = data2.get(Calendar.DAY_OF_YEAR)+bix - data1.get(Calendar.DAY_OF_YEAR);
        }
        return dias;
    }

    public Date DataPTBRStringToDate(String data_) throws Exception {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date data = null;
        try {
            data = formato.parse(data_);
            return data;
        } catch (ParseException ex) {
            throw new Exception("Erro ao converter data. " + ex);
        }
    }
    
    public Date DataMYSQLtoDate(String data_) throws Exception {
        String temp = DataMySQLtoDataStringPT(data_);
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date data = null;
        try {
            data = formato.parse(temp);
            return data;
        } catch (ParseException ex) {
            throw new Exception("Erro ao converter data. " + ex);
        }
    }
}
