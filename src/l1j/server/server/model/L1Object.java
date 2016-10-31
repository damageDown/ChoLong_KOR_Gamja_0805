/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */

package l1j.server.server.model;

import java.io.Serializable;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;

// Referenced classes of package l1j.server.server.model:
// L1PcInstance, L1Character

/**
 * 월드상에 존재하는 모든 오브젝트의 베이스 클래스
 */
public class L1Object implements Serializable {
	private static final long serialVersionUID = 1L;
	private L1Location _loc = new L1Location();
	private int _id = 0;
	private int _dir = 0;
	private int _dis = 0;

	private int _visibleX = 0;
	private int _visibleY = 0;
	private int _visibleTempX = 0;
	private int _visibleTempY = 0;
	private int _visibleMapId = 0;

	public int getVisibleX() {
		return _visibleX;
	}

	public void setVisibleX(int x) {
		_visibleX = x;
	}

	public int getVisibleY() {
		return _visibleY;
	}

	public void setVisibleY(int y) {
		_visibleY = y;
	}

	public int getVisibleTempX() {
		return _visibleTempX;
	}

	public void setVisibleTempX(int x) {
		_visibleTempX = x;
	}

	public int getVisibleTempY() {
		return _visibleTempY;
	}

	public void setVisibleTempY(int y) {
		_visibleTempY = y;
	}

	public int getVisibleMapId() {
		return _visibleMapId;
	}

	public void setVisibleMapId(int mapId) {
		_visibleMapId = mapId;
	}

	/**
	 * 오브젝트가 존재하는 MAP의 MAP ID를 돌려준다
	 * 
	 * @return MAP ID
	 */
	public short getMapId() {
		return (short) _loc.getMap().getId();
	}

	/**
	 * 오브젝트가 존재하는 MAP의 MAP ID를 설정한다
	 * 
	 * @param mapId
	 *            MAP ID
	 */
	public void setMap(short mapId) {
		_loc.setMap(L1WorldMap.getInstance().getMap(mapId));
	}

	/**
	 * 오브젝트가 존재하는 MAP를 보관 유지하는 L1Map 오브젝트를 돌려준다
	 * 
	 */
	public L1Map getMap() {
		return _loc.getMap();
	}

	/**
	 * 오브젝트가 존재하는 MAP를 설정한다
	 * 
	 * @param map
	 *            오브젝트가 존재하는 MAP를 보관 유지하는 L1Map 오브젝트
	 */
	public void setMap(L1Map map) {
		if (map == null) {
			throw new NullPointerException();
		}
		_loc.setMap(map);
	}

	/**
	 * 오브젝트를 식별하는 ID를 돌려준다
	 * 
	 * @return 오브젝트 ID
	 */
	public int getId() {
		return _id;
	}

	/**
	 * 오브젝트를 식별하는 ID를 설정한다
	 * 
	 * @param id
	 *            오브젝트 ID
	 */
	public void setId(int id) {
		_id = id;
	}

	/**
	 * 오브젝트가 존재하는 좌표의 X치를 돌려준다
	 * 
	 * @return 좌표의 X치
	 */
	public int getX() {
		return _loc.getX();
	}

	/**
	 * 오브젝트가 존재하는 좌표의 X치를 설정한다
	 * 
	 * @param x
	 *            좌표의 X치
	 */
	public void setX(int x) {
		_loc.setX(x);
	}

	public int getdir() {
		return _dir;
	}

	public void setdir(int i) {
		_dir = i;
	}

	public int getdis() {
		return _dis;
	}

	public void setdis(int i) {
		_dis = i;
	}

	/**
	 * 오브젝트가 존재하는 좌표의 Y치를 돌려준다
	 * 
	 * @return 좌표의 Y치
	 */
	public int getY() {
		return _loc.getY();
	}

	/**
	 * 오브젝트가 존재하는 좌표의 Y치를 설정한다
	 * 
	 * @param y
	 *            좌표의 Y치
	 */
	public void setY(int y) {
		_loc.setY(y);
	}

	/**
	 * 오브젝트가 존재하는 위치를 보관 유지하는, L1Location 오브젝트에의 참조를 돌려준다.
	 * 
	 * @return 좌표를 보관 유지하는, L1Location 오브젝트에의 참조
	 */
	public L1Location getLocation() {
		return _loc;
	}

	public void setLocation(L1Location loc) {
		_loc.setX(loc.getX());
		_loc.setY(loc.getY());
		_loc.setMap(loc.getMapId());
	}

	public void setLocation(int x, int y, int mapid) {
		_loc.setX(x);
		_loc.setY(y);
		_loc.setMap(mapid);
	}

	/**
	 * 지정된 오브젝트까지의 직선 거리를 돌려준다.
	 */
	public double getLineDistance(L1Object obj) {
		return this.getLocation().getLineDistance(obj.getLocation());
	}

	/**
	 * 지정된 오브젝트까지의 직선 타일수를 돌려준다.
	 */
	public int getTileLineDistance(L1Object obj) {
		return this.getLocation().getTileLineDistance(obj.getLocation());
	}

	/**
	 * 지정된 오브젝트까지의 타일수를 돌려준다.
	 */
	public int getTileDistance(L1Object obj) {
		return this.getLocation().getTileDistance(obj.getLocation());
	}

	/**
	 * 오브젝트가 플레이어의 화면내에 접어든(인식된) 때에 불려 간다.
	 * 
	 * @param perceivedFrom
	 *            이 오브젝트를 인식한 PC
	 */
	public void onPerceive(L1PcInstance perceivedFrom) {
	}

	/**
	 * 오브젝트와 액션이 발생할 때 호출
	 * 
	 * @param actionFrom
	 *            액션을 일으킨 PC
	 */
	public void onAction(L1PcInstance actionFrom) {
	}

	public void onAction(L1PcInstance actionFrom, int adddmg) {
	}

	/**
	 * 오브젝트와 대화할 때 호출
	 * 
	 * @param talkFrom
	 *            말을 건넨 PC
	 */
	public void onTalkAction(L1PcInstance talkFrom) {
	}
}
