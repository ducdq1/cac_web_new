package com.viettel.module.phamarcy.BO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import com.viettel.core.base.DAO.BaseComposer;
import com.viettel.voffice.BO.Document.Attachs;

/**
 * 
 * @author tuannt40
 *
 */
@Entity
@Table(name = "PRODUCT")
@XmlRootElement
public class Product extends BaseComposer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8823593201525222855L;

	@SequenceGenerator(name = "PRODUCT_SEQ", sequenceName = "PRODUCT_SEQ")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCT_SEQ")
	@Basic(optional = false)
	@Column(name = "PRODUCT_ID")
	private Long productId;

	@Size(max = 20)
	@Column(name = "PRODUCT_CODE")
	private String productCode;

	@Column(name = "PRODUCT_NAME")
	private String productName;

	@Column(name = "QUOTATION_NAME")
	private String quotationName;

	@Column(name = "PRICE")
	private String price;//gia nhap ban le

	@Column(name = "IMAGE")
	private String image;

	@Column(name = "PRODUCT_NAME_SEARCH")
	private String productNameSearch;

	@Column(name = "UNIT")
	private String unit;

	@Column(name = "SALE_PRICE")
	private String salePrice;//gia ban le

	@Column(name = "WARRANTY")
	private String warranty;// bao hanh

	@Column(name = "DATE_INPUT_PRICE")
	private Date dateInputPrice;//ngay nhap gia
	
	@Transient
	private String priceString;

	@Column(name = "VAT")
	private Long vat;

	@Column(name = "MADE_IN")
	private String madeIn;

	@Column(name = "PRODUCT_SIZE")
	private String size;

	@Column(name = "PRODUCT_TYPE")
	private Long productType;

	@Column(name = "FEATURE")
	private String feature;

	@Transient
	private List<Attachs> images;

	@Column(name = "DSP")
	private String dsp;

	@Column(name = "TRONG_LUONG_THUNG")
	private String trongLuongThung;
	
	@Column(name = "DONG_GOI")
	private String dongGoi;
	
	@Column(name = "NOI_SAN_XUAT")
	private String noiSanXuat;
	
	@Column(name = "CREATE_DATE")
	private Date createDate;
	
	@Column(name = "COLOR")
	private String color;
	
	@Column(name = "THONG_SO_KT")
	private String thongSoKT;
	
	@Column(name = "MA_HANG_HOA")
	private String maHangHoa;
	
	@Column(name = "MA_HANG_HOA_MOI")
	private String maHangHoaMoi;//ma hang hoa cho PM KiotViet
	
	@Column(name = "CREATE_USER")
	private String createUser;
	
	@Column(name = "PRICE_BL_KM")
	private String priceBLKM;//gia ban le KM
	
	@Column(name = "PRICE_NHAP_KM")
	private String priceNHAPKM;//gia nhap KM
	
	@Column(name = "PRICE_DL_KM")
	private String priceDLKM;//gia dai ly KM
	
	@Column(name = "PRICE_DL")
	private String priceDL;//gia ban dai ly
	
	
	@Column(name = "MA_DAI_LY")
	private String maDaiLy;
	
	
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getQuotationName() {
		return quotationName;
	}

	public void setQuotationName(String quotationName) {
		this.quotationName = quotationName;
	}

	public String getPrice() {
		if (price == null) {
			price = "";
		}
		return price;
	}

	public void setPrice(String price) {
		if (price == null) {
			price = "";
		}
		this.price = price;
		priceString = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getProductNameSearch() {
		return productNameSearch;
	}

	public void setProductNameSearch(String productNameSearch) {
		this.productNameSearch = productNameSearch;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getPriceString() {
		return priceString = price;
	}

	public void setPriceString(String priceString) {
		this.priceString = priceString;
	}

	public List<Attachs> getImages() {
		return images;
	}

	public void setImages(List<Attachs> images) {
		this.images = images;
	}

	public String getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(String salePrice) {
		this.salePrice = salePrice;
	}

	public String getWarranty() {
		return warranty;
	}

	public void setWarranty(String warranty) {
		this.warranty = warranty;
	}

	public Long getVat() {
		return vat;
	}

	public void setVat(Long vat) {
		this.vat = vat;
	}

	public String getMadeIn() {
		if (madeIn == null) {
			return "";
		}
		return madeIn;
	}

	public void setMadeIn(String madeIn) {
		this.madeIn = madeIn;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Long getProductType() {
		return productType;
	}

	public void setProductType(Long productType) {
		this.productType = productType;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public String getDsp() {
		return dsp;
	}

	public void setDsp(String dsp) {
		this.dsp = dsp;
	}

	public String getTrongLuongThung() {
		return trongLuongThung;
	}

	public void setTrongLuongThung(String trongLuongThung) {
		this.trongLuongThung = trongLuongThung;
	}

	public String getDongGoi() {
		return dongGoi;
	}

	public void setDongGoi(String dongGoi) {
		this.dongGoi = dongGoi;
	}

	public String getNoiSanXuat() {
		return noiSanXuat;
	}

	public void setNoiSanXuat(String noiSanXuat) {
		this.noiSanXuat = noiSanXuat;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getThongSoKT() {
		return thongSoKT;
	}

	public void setThongSoKT(String thongSoKT) {
		this.thongSoKT = thongSoKT;
	}

	public String getMaHangHoa() {
		return maHangHoa;
	}

	public void setMaHangHoa(String maHangHoa) {
		this.maHangHoa = maHangHoa;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getPriceBLKM() {
		return priceBLKM;
	}

	public void setPriceBLKM(String priceBLKM) {
		this.priceBLKM = priceBLKM;
	}

	public String getPriceNHAPKM() {
		return priceNHAPKM;
	}

	public void setPriceNHAPKM(String priceNHAPKM) {
		this.priceNHAPKM = priceNHAPKM;
	}

	public String getPriceDLKM() {
		return priceDLKM;
	}

	public void setPriceDLKM(String priceDLKM) {
		this.priceDLKM = priceDLKM;
	}

	public String getPriceDL() {
		return priceDL;
	}

	public void setPriceDL(String priceDL) {
		this.priceDL = priceDL;
	}

	public String getMaDaiLy() {
		return maDaiLy;
	}

	public void setMaDaiLy(String maDaiLy) {
		this.maDaiLy = maDaiLy;
	}

	public String getMaHangHoaMoi() {
		return maHangHoaMoi;
	}

	public void setMaHangHoaMoi(String maHangHoaMoi) {
		this.maHangHoaMoi = maHangHoaMoi;
	}

	public Date getDateInputPrice() {
		return dateInputPrice;
	}

	public void setDateInputPrice(Date dateInputPrice) {
		this.dateInputPrice = dateInputPrice;
	}

	 
}
