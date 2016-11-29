(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .factory('AvancementChantierSearch', AvancementChantierSearch);

    AvancementChantierSearch.$inject = ['$resource'];

    function AvancementChantierSearch($resource) {
        var resourceUrl =  'api/_search/avancement-chantiers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
