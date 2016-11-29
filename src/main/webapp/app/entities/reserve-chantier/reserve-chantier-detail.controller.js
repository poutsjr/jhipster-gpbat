(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('ReserveChantierDetailController', ReserveChantierDetailController);

    ReserveChantierDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ReserveChantier', 'Chantier'];

    function ReserveChantierDetailController($scope, $rootScope, $stateParams, previousState, entity, ReserveChantier, Chantier) {
        var vm = this;

        vm.reserveChantier = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gpbatApp:reserveChantierUpdate', function(event, result) {
            vm.reserveChantier = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
