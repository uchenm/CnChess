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
public abstract class Role implements Serializable {

    public static int RED_PLAYER = 0;
    public static int BLACK_PLAYER = 1;
    public static int OBSEVER = 2;

    // private getRole

    public Role(int roleId) {
        this.roleId = roleId;
    }

    private int roleId;

    protected int getRoleId() {
        return roleId;
    }

    protected void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public boolean isRed() {
        return this.getRoleId() == Role.RED_PLAYER;
    }

    public boolean isBlack() {
        return this.getRoleId() == Role.BLACK_PLAYER;
    }

    public boolean isObserver() {
        return this.getRoleId() == Role.OBSEVER;
    }

    public String getName() {
        final String clazzFullName = this.getClass().getName();
        return clazzFullName.substring(clazzFullName.lastIndexOf(".") + 1);
    }

    public String toString() {
        return this.getName();
    }

    public boolean equals(Role role) {
        if (role == null)
            return false;
        return (role.getRoleId() == this.getRoleId());
    }
}
