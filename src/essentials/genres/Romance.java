package essentials.genres;

import essentials.observerPattern.Subject;

public final class Romance extends Subject {
        private static final Romance ROMANCE  = new Romance();

        private Romance() {
        }

        public static Romance getInstance() {
            return ROMANCE;
        }
}
