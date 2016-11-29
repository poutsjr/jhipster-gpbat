(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('CorpsEtatDialogController', CorpsEtatDialogController);

    CorpsEtatDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CorpsEtat'];

    function CorpsEtatDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CorpsEtat) {
        var vm = this;

        vm.corpsEtat = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.corpsEtat.id !== null) {
                CorpsEtat.update(vm.corpsEtat, onSaveSuccess, onSaveError);
            } else {
                CorpsEtat.save(vm.corpsEtat, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gpbatApp:corpsEtatUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
