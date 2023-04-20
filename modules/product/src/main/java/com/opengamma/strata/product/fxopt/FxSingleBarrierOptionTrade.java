/*
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.product.fxopt;

import java.io.Serializable;
import java.util.Map;
import java.util.NoSuchElementException;

import org.joda.beans.Bean;
import org.joda.beans.ImmutableBean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaBean;
import org.joda.beans.MetaProperty;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.ImmutableDefaults;
import org.joda.beans.gen.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.strata.basics.ReferenceData;
import com.opengamma.strata.basics.currency.AdjustablePayment;
import com.opengamma.strata.basics.currency.CurrencyAmount;
import com.opengamma.strata.basics.currency.CurrencyPair;
import com.opengamma.strata.product.PortfolioItemInfo;
import com.opengamma.strata.product.PortfolioItemSummary;
import com.opengamma.strata.product.ProductType;
import com.opengamma.strata.product.ResolvableTrade;
import com.opengamma.strata.product.TradeInfo;
import com.opengamma.strata.product.common.SummarizerUtils;
import com.opengamma.strata.product.fx.FxOptionTrade;

/**
 * A trade in an FX single barrier option.
 * <p>
 * An Over-The-Counter (OTC) trade in an {@link FxSingleBarrierOption}.
 * <p>
 * An FX option is a financial instrument that provides an option to exchange two currencies at a specified future time
 * only when barrier event occurs (knock-in option) or does not occur (knock-out option).
 */
@BeanDefinition
public final class FxSingleBarrierOptionTrade
    implements FxOptionTrade, ResolvableTrade<ResolvedFxSingleBarrierOptionTrade>, ImmutableBean, Serializable {

  /**
  * The additional trade information, defaulted to an empty instance.
  * <p>
  * This allows additional information to be attached to the trade.
  */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private final TradeInfo info;
  /**
  * The FX option product that was agreed when the trade occurred.
  * <p>
  * The product captures the contracted financial details of the trade.
  */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private final FxSingleBarrierOption product;
  /**
  * The premium of the FX option.
  * <p>
  * The premium sign should be compatible with the product Long/Short flag.
  * This means that the premium is negative for long and positive for short.
  */
  @PropertyDefinition(validate = "notNull")
  private final AdjustablePayment premium;

  //-------------------------------------------------------------------------
  @ImmutableDefaults
  private static void applyDefaults(Builder builder) {
    builder.info = TradeInfo.empty();
  }

  //-------------------------------------------------------------------------
  @Override
  public FxSingleBarrierOptionTrade withInfo(PortfolioItemInfo info) {
    return new FxSingleBarrierOptionTrade(TradeInfo.from(info), product, premium);
  }

  //-------------------------------------------------------------------------
  @Override
  public PortfolioItemSummary summarize() {
    // Long Barrier Down-and-KnockIn @ 1.2 Pay USD 1mm Premium USD 100k @ GBP/USD 1.32 : 21Jan18
    StringBuilder buf = new StringBuilder(96);
    CurrencyAmount base = product.getUnderlyingOption().getUnderlying().getBaseCurrencyAmount();
    CurrencyAmount counter = product.getUnderlyingOption().getUnderlying().getCounterCurrencyAmount();
    buf.append(product.getUnderlyingOption().getLongShort());
    buf.append(" Barrier ");
    buf.append(SummarizerUtils.barrier(product.getBarrier(), product.getUnderlyingOption().getExpiryDate()));
    buf.append(" ");
    buf.append(SummarizerUtils.fx(base, counter));
    buf.append(" Premium ");
    buf.append(SummarizerUtils.amount(premium.getValue().mapAmount(v -> Math.abs(v))));
    buf.append(" : ");
    buf.append(SummarizerUtils.date(product.getUnderlyingOption().getExpiryDate()));
    CurrencyPair currencyPair = product.getCurrencyPair();
    return SummarizerUtils.summary(
        this, ProductType.FX_SINGLE_BARRIER_OPTION, buf.toString(), currencyPair.getBase(), currencyPair.getCounter());
  }

  @Override
  public ResolvedFxSingleBarrierOptionTrade resolve(ReferenceData refData) {
    return ResolvedFxSingleBarrierOptionTrade.builder()
        .info(info)
        .product(product.resolve(refData))
        .premium(premium.resolve(refData))
        .build();
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code FxSingleBarrierOptionTrade}.
   * @return the meta-bean, not null
   */
  public static FxSingleBarrierOptionTrade.Meta meta() {
    return FxSingleBarrierOptionTrade.Meta.INSTANCE;
  }

  static {
    MetaBean.register(FxSingleBarrierOptionTrade.Meta.INSTANCE);
  }

  /**
   * The serialization version id.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static FxSingleBarrierOptionTrade.Builder builder() {
    return new FxSingleBarrierOptionTrade.Builder();
  }

  private FxSingleBarrierOptionTrade(
      TradeInfo info,
      FxSingleBarrierOption product,
      AdjustablePayment premium) {
    JodaBeanUtils.notNull(info, "info");
    JodaBeanUtils.notNull(product, "product");
    JodaBeanUtils.notNull(premium, "premium");
    this.info = info;
    this.product = product;
    this.premium = premium;
  }

  @Override
  public FxSingleBarrierOptionTrade.Meta metaBean() {
    return FxSingleBarrierOptionTrade.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the additional trade information, defaulted to an empty instance.
   * <p>
   * This allows additional information to be attached to the trade.
   * @return the value of the property, not null
   */
  @Override
  public TradeInfo getInfo() {
    return info;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the FX option product that was agreed when the trade occurred.
   * <p>
   * The product captures the contracted financial details of the trade.
   * @return the value of the property, not null
   */
  @Override
  public FxSingleBarrierOption getProduct() {
    return product;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the premium of the FX option.
   * <p>
   * The premium sign should be compatible with the product Long/Short flag.
   * This means that the premium is negative for long and positive for short.
   * @return the value of the property, not null
   */
  public AdjustablePayment getPremium() {
    return premium;
  }

  //-----------------------------------------------------------------------
  /**
   * Returns a builder that allows this bean to be mutated.
   * @return the mutable builder, not null
   */
  public Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      FxSingleBarrierOptionTrade other = (FxSingleBarrierOptionTrade) obj;
      return JodaBeanUtils.equal(info, other.info) &&
          JodaBeanUtils.equal(product, other.product) &&
          JodaBeanUtils.equal(premium, other.premium);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(info);
    hash = hash * 31 + JodaBeanUtils.hashCode(product);
    hash = hash * 31 + JodaBeanUtils.hashCode(premium);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(128);
    buf.append("FxSingleBarrierOptionTrade{");
    buf.append("info").append('=').append(JodaBeanUtils.toString(info)).append(',').append(' ');
    buf.append("product").append('=').append(JodaBeanUtils.toString(product)).append(',').append(' ');
    buf.append("premium").append('=').append(JodaBeanUtils.toString(premium));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code FxSingleBarrierOptionTrade}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code info} property.
     */
    private final MetaProperty<TradeInfo> info = DirectMetaProperty.ofImmutable(
        this, "info", FxSingleBarrierOptionTrade.class, TradeInfo.class);
    /**
     * The meta-property for the {@code product} property.
     */
    private final MetaProperty<FxSingleBarrierOption> product = DirectMetaProperty.ofImmutable(
        this, "product", FxSingleBarrierOptionTrade.class, FxSingleBarrierOption.class);
    /**
     * The meta-property for the {@code premium} property.
     */
    private final MetaProperty<AdjustablePayment> premium = DirectMetaProperty.ofImmutable(
        this, "premium", FxSingleBarrierOptionTrade.class, AdjustablePayment.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "info",
        "product",
        "premium");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 3237038:  // info
          return info;
        case -309474065:  // product
          return product;
        case -318452137:  // premium
          return premium;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public FxSingleBarrierOptionTrade.Builder builder() {
      return new FxSingleBarrierOptionTrade.Builder();
    }

    @Override
    public Class<? extends FxSingleBarrierOptionTrade> beanType() {
      return FxSingleBarrierOptionTrade.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code info} property.
     * @return the meta-property, not null
     */
    public MetaProperty<TradeInfo> info() {
      return info;
    }

    /**
     * The meta-property for the {@code product} property.
     * @return the meta-property, not null
     */
    public MetaProperty<FxSingleBarrierOption> product() {
      return product;
    }

    /**
     * The meta-property for the {@code premium} property.
     * @return the meta-property, not null
     */
    public MetaProperty<AdjustablePayment> premium() {
      return premium;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 3237038:  // info
          return ((FxSingleBarrierOptionTrade) bean).getInfo();
        case -309474065:  // product
          return ((FxSingleBarrierOptionTrade) bean).getProduct();
        case -318452137:  // premium
          return ((FxSingleBarrierOptionTrade) bean).getPremium();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      metaProperty(propertyName);
      if (quiet) {
        return;
      }
      throw new UnsupportedOperationException("Property cannot be written: " + propertyName);
    }

  }

  //-----------------------------------------------------------------------
  /**
   * The bean-builder for {@code FxSingleBarrierOptionTrade}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<FxSingleBarrierOptionTrade> {

    private TradeInfo info;
    private FxSingleBarrierOption product;
    private AdjustablePayment premium;

    /**
     * Restricted constructor.
     */
    private Builder() {
      applyDefaults(this);
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(FxSingleBarrierOptionTrade beanToCopy) {
      this.info = beanToCopy.getInfo();
      this.product = beanToCopy.getProduct();
      this.premium = beanToCopy.getPremium();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 3237038:  // info
          return info;
        case -309474065:  // product
          return product;
        case -318452137:  // premium
          return premium;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 3237038:  // info
          this.info = (TradeInfo) newValue;
          break;
        case -309474065:  // product
          this.product = (FxSingleBarrierOption) newValue;
          break;
        case -318452137:  // premium
          this.premium = (AdjustablePayment) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public Builder set(MetaProperty<?> property, Object value) {
      super.set(property, value);
      return this;
    }

    @Override
    public FxSingleBarrierOptionTrade build() {
      return new FxSingleBarrierOptionTrade(
          info,
          product,
          premium);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the additional trade information, defaulted to an empty instance.
     * <p>
     * This allows additional information to be attached to the trade.
     * @param info  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder info(TradeInfo info) {
      JodaBeanUtils.notNull(info, "info");
      this.info = info;
      return this;
    }

    /**
     * Sets the FX option product that was agreed when the trade occurred.
     * <p>
     * The product captures the contracted financial details of the trade.
     * @param product  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder product(FxSingleBarrierOption product) {
      JodaBeanUtils.notNull(product, "product");
      this.product = product;
      return this;
    }

    /**
     * Sets the premium of the FX option.
     * <p>
     * The premium sign should be compatible with the product Long/Short flag.
     * This means that the premium is negative for long and positive for short.
     * @param premium  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder premium(AdjustablePayment premium) {
      JodaBeanUtils.notNull(premium, "premium");
      this.premium = premium;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(128);
      buf.append("FxSingleBarrierOptionTrade.Builder{");
      buf.append("info").append('=').append(JodaBeanUtils.toString(info)).append(',').append(' ');
      buf.append("product").append('=').append(JodaBeanUtils.toString(product)).append(',').append(' ');
      buf.append("premium").append('=').append(JodaBeanUtils.toString(premium));
      buf.append('}');
      return buf.toString();
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
