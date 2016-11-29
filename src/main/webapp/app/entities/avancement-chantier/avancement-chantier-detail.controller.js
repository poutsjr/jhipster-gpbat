(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('AvancementChantierDetailController', AvancementChantierDetailController);

    AvancementChantierDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AvancementChantier', 'Chantier'];

    function AvancementChantierDetailController($scope, $rootScope, $stateParams, previousState, entity, AvancementChantier, Chantier) {
        var vm = this;

        vm.avancementChantier = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gpbatApp:avancementChantierUpdate', function(event, result) {
            vm.avancementChantier = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
