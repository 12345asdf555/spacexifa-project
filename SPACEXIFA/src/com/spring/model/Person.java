package com.spring.model;

import java.math.BigInteger;
import java.util.Date;

public class Person {
	private BigInteger id;
	private String welderno;
	private String name;
	private String cellphone;
	private int leveid;
	private String cardnum;
	private int quali;
	private Date createdate;
	private Date updatedate;
	private BigInteger creater;
	private BigInteger updater;
	private BigInteger owner;
	private BigInteger insid;
	private String insname;
	private String back;
	private Integer val;
	private int type;
	private String valuename;
	private String valuenamex;
	private String qualiname;
	private String levename;
	private int FSTATUS; //焊工状态：0离线（默认）

	public Person(){
		super();
	}
	
	public String getQualiname() {
		return qualiname;
	}

	public void setQualiname(String qualiname) {
		this.qualiname = qualiname;
	}

	public String getLevename() {
		return levename;
	}

	public void setLevename(String levename) {
		this.levename = levename;
	}

	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public String getWelderno() {
		return welderno;
	}
	public void setWelderno(String welderno) {
		this.welderno = welderno;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCellphone() {
		return cellphone;
	}
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	public String getInsname() {
		return insname;
	}
	public void setInsname(String insname) {
		this.insname = insname;
	}
	public int getLeveid() {
		return leveid;
	}
	public void setLeveid(int leveid) {
		this.leveid = leveid;
	}
	public BigInteger getCreater() {
		return creater;
	}
	public void setCreater(BigInteger creater) {
		this.creater = creater;
	}
	public int getQuali() {
		return quali;
	}
	public void setQuali(int quali) {
		this.quali = quali;
	}
	public BigInteger getUpdater() {
		return updater;
	}
	public void setUpdater(BigInteger updater) {
		this.updater = updater;
	}
	public BigInteger getOwner() {
		return owner;
	}
	public void setOwner(BigInteger owner) {
		this.owner = owner;
	}
	public BigInteger getInsid() {
		return insid;
	}
	public void setInsid(BigInteger insid) {
		this.insid = insid;
	}
	public String getCardnum() {
		return cardnum;
	}
	public void setCardnum(String cardnum) {
		this.cardnum = cardnum;
	}
	public String getBack() {
		return back;
	}
	public void setBack(String back) {
		this.back = back;
	}
	public Date getCreatedate(){
		return createdate;
	}
	public void setCreatedate(Date createdate){
		this.createdate = createdate;
	}
	public Date getUpdatedate(){
		return updatedate;
	}
	public void setUpdatedate(Date updatedate){
		this.updatedate = updatedate;
	}
	public int getVal(){
		return val;
	}
	public void setVal(int val){
		this.val = val;
	}
	public int getType(){
		return type;
	}
	public void setType(int type){
		this.type = type;
	}
	public String getValuename(){
		return valuename;
	}
	public void setValuename(String valuename){
		this.valuename = valuename;
	}
	public String getValuenamex(){
		return valuenamex;
	}
	public void setValuenamex(String valuenamex){
		this.valuenamex = valuenamex;
	}

	public int getFSTATUS() {
		return FSTATUS;
	}

	public void setFSTATUS(int FSTATUS) {
		this.FSTATUS = FSTATUS;
	}
}