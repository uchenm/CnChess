/**
 * Copyright 2013  Ming Chen<uchenm@gmail.com>

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package chess.model.player;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Player implements Serializable {

    private String  name;

    private Role    role;

    private boolean creator;

    public Player(String name) {
        this(name, new ObseverRole());
    }

    public Player(String name, Role r) {
        this(name, r, false);
    }

    public Player(String name, Role role, boolean isCreator) {
        this.name = name;
        this.role = role;
        this.creator = isCreator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isCreator() {
        return creator;
    }

    public void setCreator(boolean creator) {
        this.creator = creator;
    }

    public String toString() {
        return "player[name=" + this.getName() + ",role="
                + (getRole() != null ? role.toString() : "") + ",isCreator="
                + isCreator() + "]";
    }

    public boolean equals(Player p) {
        if (p == null)
            return false;
        return (p.name.equals(name) && p.role.equals(role) && p.creator == creator);
    }
}
