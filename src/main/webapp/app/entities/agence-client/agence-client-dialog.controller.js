(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('AgenceClientDialogController', AgenceClientDialogController);

    AgenceClientDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AgenceClient', 'Client'];

    function AgenceClientDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AgenceClient, Client) {
        var vm = this;

        vm.agenceClient = entity;
        vm.clear = clear;
        vm.save = save;
        vm.clients = Client.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.agenceClient.id !== null) {
                AgenceClient.update(vm.agenceClient, onSaveSuccess, onSaveError);
            } else {
                AgenceClient.save(vm.agenceClient, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gpbatApp:agenceClientUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
