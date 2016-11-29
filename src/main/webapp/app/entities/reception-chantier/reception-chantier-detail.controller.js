(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('ReceptionChantierDetailController', ReceptionChantierDetailController);

    ReceptionChantierDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ReceptionChantier'];

    function ReceptionChantierDetailController($scope, $rootScope, $stateParams, previousState, entity, ReceptionChantier) {
        var vm = this;

        vm.receptionChantier = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gpbatApp:receptionChantierUpdate', function(event, result) {
            vm.receptionChantier = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
