package com.jho5245.cucumbery.commands.brigadier.base;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;

public abstract class CommandBase
{
  public static CommandAPICommand getCommandBase(String command, String permission, String... aliases)
  {
    CommandAPICommand commandAPICommand = new CommandAPICommand(command);
    if (aliases != null && aliases.length > 0)
    {
      commandAPICommand = commandAPICommand.withAliases(aliases);
    }
    commandAPICommand = commandAPICommand.withPermission(CommandPermission.fromString(permission));
    switch (command)
    {
      case "ride" -> commandAPICommand = commandAPICommand.withHelp("개체 탑승 명령어입니다", "개체를 탑승하거나 탑승을 중지합니다");
      case "sudo2" -> commandAPICommand = commandAPICommand.withHelp("플레이어에게 명령어를 강제로 실행합니다", "플레이어에게 명령어를 강제로 실행합니다");
      case "cgive" -> commandAPICommand = commandAPICommand.withHelp("플레이어에게 아이템을 지급합니다", "플레이어에게 아이템을 지급합니다");
      case "velocity" -> commandAPICommand = commandAPICommand.withHelp("개체를 날려버립니다", "개체에게 방향 벡터를 적용합니다");
      case "healthpoint" -> commandAPICommand = commandAPICommand.withHelp("개체의 HP를 조정합니다", "개체의 HP를 지급/차감/설정합니다");
      case "ckill" -> commandAPICommand = commandAPICommand.withHelp("개체를 삭제합니다", "개체를 삭제합니다 플레이어의 경우에는 HP를 0으로 설정합니다");
      case "setitem" -> commandAPICommand = commandAPICommand.withHelp("개체의 슬롯 아이템을 수정합니다", "주로 사용하는 손에 들고 있는 아이템을 개체의 슬롯에 붙여넣습니다");
      case "consolesudo2" -> commandAPICommand = commandAPICommand.withHelp("콘솔로 명령어를 실행합니다", "콘솔로 명령어를 실행합니다");
      case "sendactionbar" -> commandAPICommand = commandAPICommand.withHelp("플레이어에게 액션바를 보여줍니다", "플레이어에게 액션바를 보여줍니다");
      case "sendtitle" -> commandAPICommand = commandAPICommand.withHelp("플레이어에게 타이틀을 보여줍니다", "타이틀 형식은 '타이틀;서브타이틀;페이드인;스테이;페이드아웃' 입니다");
      case "updateitem" -> commandAPICommand = commandAPICommand.withHelp("바닥에 떨어져 있는 아이템의 NBT를 업데이트합니다", "바닥에 떨어져 있는 아이템의 NBT를 업데이트합니다");
      case "ceffect" -> commandAPICommand = commandAPICommand.withHelp("개체에게 효과를 주는 명령어입니다", "개체에게 효과를 지급합니다");
      case "damage" -> commandAPICommand = commandAPICommand.withHelp("개체에게 피해를 입힙니다", "개체에게 피해를 입힙니다");
      case "csummon" -> commandAPICommand = commandAPICommand.withHelp("개체를 소환합니다", "개체를 여러개 소환합니다");
      case "csetblock" -> commandAPICommand = commandAPICommand.withHelp("블록의 종류 변경 없이 블록 데이터만 변경합니다", "블록의 종류 변경 없이 블록 데이터만 변경합니다");
      case "replaceentity" -> commandAPICommand = commandAPICommand.withHelp("개체의 유형을 변경합니다", "개체의 유형을 변경합니다");
      case "crepeat" -> commandAPICommand = commandAPICommand.withHelp("명령어를 반복 실행합니다", "명령어를 반복 실행합니다");
      case "cdata" -> commandAPICommand = commandAPICommand.withHelp("개체의 데이터를 수정합니다", "플레이어의 데이터도 수정할 수 있습니다");
      case "teleport2" -> commandAPICommand = commandAPICommand.withHelp("특정 좌표로 이동합니다", "월드 스폰, 바라보고 있는 블록, 침대 스폰 등.");
      case "explode" -> commandAPICommand = commandAPICommand.withHelp("폭발을 일으킵니다", "펑 에케서버");
      case "teleport" -> commandAPICommand = commandAPICommand.withHelp("텔레포트 명령어입니다", "개체를 입력하지 않아도 x,y축 회전 인수를 사용할 수 있습니다");
      case "sellitem" -> commandAPICommand = commandAPICommand.withHelp("아이템을 판매합니다", "팔아요 싸요.");
      case "clear2" -> commandAPICommand = commandAPICommand.withHelp("아이템을 제거합니다", "입력한 Predicate에 적합하지 않은 아이템을 전부 제거합니다");
      case "search-container-item" -> commandAPICommand = commandAPICommand.withHelp("아이템이 들어있는 컨테이너를 검색합니다", "근처에 있는 특정 아이템이 들어있는 컨테이너의 위치를 참조합니다");
    }
    return commandAPICommand;
  }

  public abstract void registerCommand(String command, String permission, String... aliases);
}
