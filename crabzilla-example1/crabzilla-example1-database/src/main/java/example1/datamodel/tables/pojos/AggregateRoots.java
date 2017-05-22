/*
 * This file is generated by jOOQ.
*/
package example1.datamodel.tables.pojos;


import javax.annotation.Generated;
import java.io.Serializable;
import java.sql.Timestamp;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.2"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class AggregateRoots implements Serializable {

    private static final long serialVersionUID = 1906557345;

    private String    arName;
    private String    arId;
    private Long      version;
    private Timestamp lastUpdatedOn;

    public AggregateRoots() {}

    public AggregateRoots(AggregateRoots value) {
        this.arName = value.arName;
        this.arId = value.arId;
        this.version = value.version;
        this.lastUpdatedOn = value.lastUpdatedOn;
    }

    public AggregateRoots(
        String    arName,
        String    arId,
        Long      version,
        Timestamp lastUpdatedOn
    ) {
        this.arName = arName;
        this.arId = arId;
        this.version = version;
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public String getArName() {
        return this.arName;
    }

    public AggregateRoots setArName(String arName) {
        this.arName = arName;
        return this;
    }

    public String getArId() {
        return this.arId;
    }

    public AggregateRoots setArId(String arId) {
        this.arId = arId;
        return this;
    }

    public Long getVersion() {
        return this.version;
    }

    public AggregateRoots setVersion(Long version) {
        this.version = version;
        return this;
    }

    public Timestamp getLastUpdatedOn() {
        return this.lastUpdatedOn;
    }

    public AggregateRoots setLastUpdatedOn(Timestamp lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("AggregateRoots (");

        sb.append(arName);
        sb.append(", ").append(arId);
        sb.append(", ").append(version);
        sb.append(", ").append(lastUpdatedOn);

        sb.append(")");
        return sb.toString();
    }
}