package rs.etf.ga070530.monopoly.database;

public class MonopolyDBSchema {

    public static final class PlayerTable{
        public static final String PLAYER = "player";
//        public static final String PLAYER_2 = "player2";
//        public static final String PLAYER_3 = "player3";
//        public static final String PLAYER_4 = "player4";

        public static final class PlayerCols{
            public static final String ID = "id";
            public static final String NAME = "name";
            public static final String TYPE = "type";
            public static final String MONEY = "money";
        }
    }

    public static final class SettingsTable{
        public static final String NAME = "settings";

        public static final class Cols{
            public static final String ID = "id";
            public static final String INIT_CASH = "cash";
            public static final String GO_PASS = "pass";
            public static final String GO_LAND = "land";
        }
    }

    public static final class CardsTable{
        public static final String CARDS_TABLE = "cards";

        public static final class CCols{
            public static final String ID = "id";
            public static final String NAME = "cards";
            public static final String X_COORD = "x";
            public static final String Y_COORD = "y";
            public static final String PRICE = "price";
            public static final String BUYABLE = "buyable";
            public static final String HOUSES = "houses";
        }
    }

    public static final class PlayerCards{
        public static final String PLAYER_CARDS = "player_cards";

        public static final class Cards{
            public static final String PLAYER_ID = "player_id";
            public static final String CARD_ID = "card_id";
        }
    }

    public static final class CardGroupsTable {
        public static final String CARD_GROUPS = "groups";

        public static final class GroupCols{
            public static final String ID = "id";
            public static final String CARD_1 = "card_1";
            public static final String CARD_2 = "card_2";
            public static final String CARD_3 = "card_3";
            public static final String CARD_4 = "card_4";
        }
    }

    public static final class StatisticTable{
        public static final String STATISTIC_NAME = "statistic";

        public static final class StatisticCols{
            public static final String ID = "id";
            public static final String GAME_ID = "game_id";
            public static final String PLAYERS = "players";
            public static final String MOVE = "move";
            public static final String START_TIME = "start_time";
            public static final String DURATION = "duration";
            public static final String WINNER = "winner";
            public static final String MONEY = "money";
        }
    }
}
