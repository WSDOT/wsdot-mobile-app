/*
 * Copyright (c) 2014 Washington State Department of Transportation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 */

package gov.wa.wsdot.mobile.shared;

import java.io.Serializable;

public class FerriesTerminalSailingSpaceItem implements Serializable {

    private static final long serialVersionUID = 7501823398672454545L;
    private int terminalId;
    private String terminalName;
    private String terminalAbbrev;
    private String terminalDepartingSpaces;
    private String lastUpdated;
    private int isStarred;
    
    public int getTerminalId() {
        return terminalId;
    }
    
    public void setTerminalId(int terminalId) {
        this.terminalId = terminalId;
    }
    
    public String getTerminalName() {
        return terminalName;
    }
    
    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }
    
    public String getTerminalAbbrev() {
        return terminalAbbrev;
    }
    
    public void setTerminalAbbrev(String terminalAbbrev) {
        this.terminalAbbrev = terminalAbbrev;
    }
    
    public String getTerminalDepartingSpaces() {
        return terminalDepartingSpaces;
    }
    
    public void setTerminalDepartingSpaces(String terminalDepartingSpaces) {
        this.terminalDepartingSpaces = terminalDepartingSpaces;
    }
    
    public String getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    public int getIsStarred() {
        return isStarred;
    }
    
    public void setIsStarred(int isStarred) {
        this.isStarred = isStarred;
    }
}
