(function() {
  "use strict";

  var HOURS_24 = moment.duration(24, 'hour').asMilliseconds();
  var HOUR_1   = moment.duration(1, 'hour').asMilliseconds();
  var MINUTE_1 = moment.duration(1, 'minute').asMilliseconds();

  moment.duration.fn.humanizeForGoCD = function () {
    var d = moment.duration(this, "ms");

    if (d >= HOURS_24) {
      return d.format("d[d] h:m:s.S");
    }

    if (d >= HOUR_1) {
      return d.format("h[h] m[m] s.S[s]");
    }

    if (d >= MINUTE_1) {
      return d.format("m[m] s.S[s]");
    }

    return d.format("s.S[s]", {trim: false});
  };
})();
