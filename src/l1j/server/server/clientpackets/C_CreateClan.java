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

package l1j.server.server.clientpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket
import l1j.server.server.utils.SQLUtil;
import server.LineageClient;

public class C_CreateClan extends ClientBasePacket {

	private static final String C_CREATE_CLAN = "[C] C_CreateClan";

	public C_CreateClan(byte abyte0[], LineageClient clientthread)
			throws Exception {
		super(abyte0);
		try {
			String s = readS();

			if (s == null || s.equalsIgnoreCase(""))
				return;

			L1PcInstance l1pcinstance = clientthread.getActiveChar();

			for (char ac : s.toCharArray()) {
				if (!Character.isLetterOrDigit(ac)) {
					S_SystemMessage sm = new S_SystemMessage(
							"혈맹 창설 불가 : 잘못된 문자또는 기호가 포함됨. (" + ac + ")");
					l1pcinstance.sendPackets(sm, true);
					return;
				}
			}

			int numOfNameBytes = 0;
			numOfNameBytes = s.getBytes("EUC-KR").length;

			if (l1pcinstance.isCrown()) { // 프린스 또는 프린세스
				if (l1pcinstance.getClanid() == 0) {

					/*
					 * int length = s.length(); char chr; for(int i = 0; i <
					 * length; i++){ chr = s.charAt(i);
					 * if(Character.UnicodeBlock.of(chr) !=
					 * Character.UnicodeBlock.BASIC_LATIN){ S_SystemMessage sm =
					 * new S_SystemMessage("혈맹 창설 불가 : 한글 또는 영문 숫자만 가능");
					 * l1pcinstance.sendPackets(sm, true); return; } }
					 */

					if (!l1pcinstance.getInventory().checkItem(40308, 30000)) {
						S_SystemMessage sm = new S_SystemMessage(
								"혈맹 창설 불가 : (30000) 아데나 필요.");
						l1pcinstance.sendPackets(sm, true);
						return;
					}

					l1pcinstance.getInventory().consumeItem(L1ItemId.ADENA,
							30000);

					if (l1pcinstance.getLevel() < 5) {
						S_SystemMessage sm = new S_SystemMessage(
								"5레벨 미만은 혈맹을 창설할수 없습니다.");
						l1pcinstance.sendPackets(sm, true);
						return;
					}
					for (int i = 0; i < s.length(); i++) {
						if (s.charAt(i) == ' ' || s.charAt(i) == 'ㅤ') {
							S_ServerMessage sm = new S_ServerMessage(53);
							l1pcinstance.sendPackets(sm, true);
							return;
						}
					}
					if (8 < (numOfNameBytes - s.length())
							|| 16 < numOfNameBytes) {

						S_SystemMessage sm = new S_SystemMessage(
								"혈맹이름이 너무 깁니다.");
						l1pcinstance.sendPackets(sm, true);
					}

					S_SystemMessage sm99 = new S_SystemMessage(
							"같은 이름의 혈맹이 존재합니다.");
					if (s.equalsIgnoreCase("ruphy")) {
						l1pcinstance.sendPackets(sm99, true);
						return;
					}

					for (L1Clan clan : L1World.getInstance().getAllClans()) { // \f1
																				// 같은
																				// 이름의
																				// 혈맹이
																				// 존재합니다.
						if (clan.getClanName().toLowerCase()
								.equals(s.toLowerCase())) {
							l1pcinstance.sendPackets(sm99, true);
							return;
						}
					}

					if (gambleClanCheck(s)) {
						l1pcinstance.sendPackets(sm99, true);
						return;
					}
					L1Clan clan = ClanTable.getInstance().createClan(
							l1pcinstance, s); // 크란 창설

					if (clan != null) {
						S_ServerMessage sm = new S_ServerMessage(84, s);
						l1pcinstance.sendPackets(sm, true);
					}

				} else {

					S_SystemMessage sm = new S_SystemMessage("이미 혈맹을 창설하였습니다");
					l1pcinstance.sendPackets(sm, true);
				}
			} else {

				S_SystemMessage sm = new S_SystemMessage(
						"왕자와 공주만이 혈맹을 창설할 수 있습니다.");
				l1pcinstance.sendPackets(sm, true);
			}
		} catch (Exception e) {

		} finally {
			clear();
		}
	}

	private boolean gambleClanCheck(String s) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT * FROM npc_gamble_spawnlist WHERE clan_name=?");
			pstm.setString(1, s);
			rs = pstm.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return false;
	}

	@Override
	public String getType() {
		return C_CREATE_CLAN;
	}

}
